package com.noxchat.app.ui.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.noxchat.app.data.ChatStore
import com.noxchat.app.ui.theme.BubbleMine
import com.noxchat.app.ui.theme.BubbleOther
import com.noxchat.app.ui.theme.TextSecondary

@Composable
fun ChatScreen(
    chatId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val chat = ChatStore.chats.firstOrNull { it.id == chatId } ?: return

    var input by remember { mutableStateOf("") }
    var isTyping by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    LaunchedEffect(chat.messages.size) {
        if (chat.messages.isNotEmpty()) {
            listState.animateScrollToItem(chat.messages.lastIndex)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Назад",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(chat.user.avatarColor)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = chat.user.name.first().toString(),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.size(12.dp))

            Text(
                text = chat.user.name,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        if (chat.messages.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Начните переписку",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary
                )
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = chat.messages,
                    key = { message -> message.id }
                ) { message ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(tween(180)) + scaleIn(
                            initialScale = 0.94f,
                            animationSpec = tween(180)
                        )
                    ) {
                        MessageBubble(
                            text = message.text,
                            isMine = message.isMine,
                            time = message.time
                        )
                    }
                }
            }
        }

        AnimatedVisibility(visible = isTyping) {
            Text(
                text = "Печатает…",
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary,
                modifier = Modifier.padding(start = 20.dp, bottom = 6.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                placeholder = {
                    Text(text = "Сообщение")
                },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.size(10.dp))

            IconButton(
                onClick = {
                    ChatStore.sendMessage(
                        chatId = chatId,
                        text = input
                    )

                    input = ""
                    isTyping = true
                },
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Send,
                    contentDescription = "Отправить",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun MessageBubble(
    text: String,
    isMine: Boolean,
    time: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMine) {
            Arrangement.End
        } else {
            Arrangement.Start
        }
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 290.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomStart = if (isMine) 20.dp else 6.dp,
                        bottomEnd = if (isMine) 6.dp else 20.dp
                    )
                )
                .background(if (isMine) BubbleMine else BubbleOther)
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = time,
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary
            )
        }
    }
}