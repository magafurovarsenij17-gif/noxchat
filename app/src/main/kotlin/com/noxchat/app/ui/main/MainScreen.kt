package com.noxchat.app.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.noxchat.app.ui.chats.ChatsScreen
import com.noxchat.app.ui.contacts.ContactsScreen
import com.noxchat.app.ui.profile.ProfileScreen

private data class BottomTab(
    val title: String,
    val icon: ImageVector
)

@Composable
fun MainScreen(
    onOpenChat: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val tabs = listOf(
        BottomTab(
            title = "Чаты",
            icon = Icons.AutoMirrored.Outlined.Chat
        ),
        BottomTab(
            title = "Контакты",
            icon = Icons.Outlined.Person
        ),
        BottomTab(
            title = "Профиль",
            icon = Icons.Outlined.Settings
        )
    )

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title
                            )
                        },
                        label = {
                            Text(text = tab.title)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        when (selectedTab) {
            0 -> ChatsScreen(
                onOpenChat = onOpenChat,
                modifier = Modifier.padding(innerPadding)
            )

            1 -> ContactsScreen(
                onOpenChat = onOpenChat,
                modifier = Modifier.padding(innerPadding)
            )

            else -> ProfileScreen(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}