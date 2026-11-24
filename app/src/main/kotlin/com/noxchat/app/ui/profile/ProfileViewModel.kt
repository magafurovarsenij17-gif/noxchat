package com.noxchat.app.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.noxchat.app.data.SettingsRepository
import com.noxchat.app.data.UserSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = SettingsRepository(application)

    private val _settings = MutableStateFlow(
        repository.loadSettings()
    )

    val settings: StateFlow<UserSettings> = _settings.asStateFlow()

    fun updateDisplayName(value: String) {
        viewModelScope.launch {
            repository.saveDisplayName(value)

            _settings.value = _settings.value.copy(
                displayName = value.ifBlank {
                    "Пользователь"
                }
            )
        }
    }

    fun updateNotifications(value: Boolean) {
        viewModelScope.launch {
            repository.saveNotifications(value)

            _settings.value = _settings.value.copy(
                notificationsEnabled = value
            )
        }
    }

    fun updateMessageSounds(value: Boolean) {
        viewModelScope.launch {
            repository.saveMessageSounds(value)

            _settings.value = _settings.value.copy(
                messageSoundsEnabled = value
            )
        }
    }

    fun updateVibration(value: Boolean) {
        viewModelScope.launch {
            repository.saveVibration(value)

            _settings.value = _settings.value.copy(
                vibrationEnabled = value
            )
        }
    }

    fun updateReadReceipts(value: Boolean) {
        viewModelScope.launch {
            repository.saveReadReceipts(value)

            _settings.value = _settings.value.copy(
                showReadReceipts = value
            )
        }
    }

    fun updateLastSeen(value: Boolean) {
        viewModelScope.launch {
            repository.saveLastSeen(value)

            _settings.value = _settings.value.copy(
                showLastSeen = value
            )
        }
    }

    fun updateAutoDownloadMedia(value: Boolean) {
        viewModelScope.launch {
            repository.saveAutoDownloadMedia(value)

            _settings.value = _settings.value.copy(
                autoDownloadMedia = value
            )
        }
    }

    fun updateMessageTextSize(value: Int) {
        viewModelScope.launch {
            repository.saveMessageTextSize(value)

            _settings.value = _settings.value.copy(
                messageTextSize = value
            )
        }
    }

    fun updateLanguage(value: String) {
        viewModelScope.launch {
            repository.saveLanguage(value)

            _settings.value = _settings.value.copy(
                language = value
            )
        }
    }
}