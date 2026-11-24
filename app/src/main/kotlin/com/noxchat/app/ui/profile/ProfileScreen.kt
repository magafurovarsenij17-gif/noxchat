package com.noxchat.app.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.noxchat.app.ui.theme.AccentViolet
import com.noxchat.app.ui.theme.TextSecondary

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel()
) {
    val settings by viewModel.settings.collectAsState()

    var isEditingName by remember { mutableStateOf(false) }
    var nameInput by remember { mutableStateOf(settings.displayName) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Профиль",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 20.dp, bottom = 24.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(26.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(AccentViolet),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Аватар",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isEditingName) {
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = {
                        Text(text = "Имя")
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        viewModel.updateDisplayName(nameInput)
                        isEditingName = false
                    },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Сохранить")
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = settings.displayName,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    IconButton(onClick = {
                        nameInput = settings.displayName
                        isEditingName = true
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Изменить имя",
                            tint = AccentViolet
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        SettingsCard(title = "Уведомления") {
            SettingsSwitchRow(
                icon = Icons.Outlined.Notifications,
                title = "Уведомления",
                subtitle = "Показывать новые сообщения",
                isChecked = settings.notificationsEnabled,
                onCheckedChange = viewModel::updateNotifications
            )

            SettingsSwitchRow(
                icon = Icons.Outlined.VolumeUp,
                title = "Звук сообщений",
                subtitle = "Звук при новом сообщении",
                isChecked = settings.messageSoundsEnabled,
                onCheckedChange = viewModel::updateMessageSounds
            )

            SettingsSwitchRow(
                icon = Icons.Outlined.Vibration,
                title = "Вибрация",
                subtitle = "Вибрация при уведомлении",
                isChecked = settings.vibrationEnabled,
                onCheckedChange = viewModel::updateVibration
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        SettingsCard(title = "Чаты") {
            SettingsSliderRow(
                icon = Icons.Outlined.TextFields,
                title = "Размер текста",
                value = settings.messageTextSize,
                valueText = "${settings.messageTextSize} sp",
                onValueChange = viewModel::updateMessageTextSize
            )

            SettingsSwitchRow(
                icon = Icons.Outlined.Image,
                title = "Автозагрузка медиа",
                subtitle = "Загружать фото и видео автоматически",
                isChecked = settings.autoDownloadMedia,
                onCheckedChange = viewModel::updateAutoDownloadMedia
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        SettingsCard(title = "Приватность") {
            SettingsSwitchRow(
                icon = Icons.Outlined.Visibility,
                title = "Отчёты о прочтении",
                subtitle = "Показывать, когда сообщение прочитано",
                isChecked = settings.showReadReceipts,
                onCheckedChange = viewModel::updateReadReceipts
            )

            SettingsSwitchRow(
                icon = Icons.Outlined.Person,
                title = "Время последнего визита",
                subtitle = "Показывать статус «онлайн» и время визита",
                isChecked = settings.showLastSeen,
                onCheckedChange = viewModel::updateLastSeen
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        SettingsCard(title = "Общие") {
            SettingsSwitchRow(
                icon = Icons.Outlined.Archive,
                title = "Язык интерфейса",
                subtitle = settings.language,
                isChecked = false,
                onCheckedChange = {
                    val newLanguage = if (settings.language == "Русский") {
                        "English"
                    } else {
                        "Русский"
                    }

                    viewModel.updateLanguage(newLanguage)
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}