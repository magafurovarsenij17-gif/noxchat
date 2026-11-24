package com.noxchat.app.data

import android.content.Context
import android.content.SharedPreferences

data class UserSettings(
    val displayName: String,
    val notificationsEnabled: Boolean,
    val messageSoundsEnabled: Boolean,
    val vibrationEnabled: Boolean,
    val showReadReceipts: Boolean,
    val showLastSeen: Boolean,
    val autoDownloadMedia: Boolean,
    val messageTextSize: Int,
    val language: String
)

class SettingsRepository(
    private val context: Context
) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences("nox_settings", Context.MODE_PRIVATE)

    fun loadSettings(): UserSettings {
        return UserSettings(
            displayName = preferences.getString(
                KEY_DISPLAY_NAME,
                "Пользователь"
            ) ?: "Пользователь",

            notificationsEnabled = preferences.getBoolean(
                KEY_NOTIFICATIONS,
                true
            ),

            messageSoundsEnabled = preferences.getBoolean(
                KEY_MESSAGE_SOUNDS,
                true
            ),

            vibrationEnabled = preferences.getBoolean(
                KEY_VIBRATION,
                true
            ),

            showReadReceipts = preferences.getBoolean(
                KEY_READ_RECEIPTS,
                true
            ),

            showLastSeen = preferences.getBoolean(
                KEY_LAST_SEEN,
                true
            ),

            autoDownloadMedia = preferences.getBoolean(
                KEY_AUTO_DOWNLOAD,
                true
            ),

            messageTextSize = preferences.getInt(
                KEY_MESSAGE_TEXT_SIZE,
                16
            ),

            language = preferences.getString(
                KEY_LANGUAGE,
                "Русский"
            ) ?: "Русский"
        )
    }

    fun saveDisplayName(value: String) {
        preferences.edit()
            .putString(KEY_DISPLAY_NAME, value.ifBlank { "Пользователь" })
            .apply()
    }

    fun saveNotifications(value: Boolean) {
        preferences.edit()
            .putBoolean(KEY_NOTIFICATIONS, value)
            .apply()
    }

    fun saveMessageSounds(value: Boolean) {
        preferences.edit()
            .putBoolean(KEY_MESSAGE_SOUNDS, value)
            .apply()
    }

    fun saveVibration(value: Boolean) {
        preferences.edit()
            .putBoolean(KEY_VIBRATION, value)
            .apply()
    }

    fun saveReadReceipts(value: Boolean) {
        preferences.edit()
            .putBoolean(KEY_READ_RECEIPTS, value)
            .apply()
    }

    fun saveLastSeen(value: Boolean) {
        preferences.edit()
            .putBoolean(KEY_LAST_SEEN, value)
            .apply()
    }

    fun saveAutoDownloadMedia(value: Boolean) {
        preferences.edit()
            .putBoolean(KEY_AUTO_DOWNLOAD, value)
            .apply()
    }

    fun saveMessageTextSize(value: Int) {
        preferences.edit()
            .putInt(KEY_MESSAGE_TEXT_SIZE, value)
            .apply()
    }

    fun saveLanguage(value: String) {
        preferences.edit()
            .putString(KEY_LANGUAGE, value)
            .apply()
    }

    private companion object {
        const val KEY_DISPLAY_NAME = "display_name"
        const val KEY_NOTIFICATIONS = "notifications_enabled"
        const val KEY_MESSAGE_SOUNDS = "message_sounds_enabled"
        const val KEY_VIBRATION = "vibration_enabled"
        const val KEY_READ_RECEIPTS = "show_read_receipts"
        const val KEY_LAST_SEEN = "show_last_seen"
        const val KEY_AUTO_DOWNLOAD = "auto_download_media"
        const val KEY_MESSAGE_TEXT_SIZE = "message_text_size"
        const val KEY_LANGUAGE = "language"
    }
}