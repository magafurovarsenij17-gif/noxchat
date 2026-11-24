package com.noxchat.app.data

import androidx.room.withTransaction
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlinx.coroutines.flow.Flow

class ChatRepository(
    private val database: ChatDatabase
) {

    private val chatDao = database.chatDao()
    private val messageDao = database.messageDao()

    fun observeChats(): Flow<List<ChatEntity>> {
        return chatDao.observeActiveChats()
    }

    fun observeArchivedChats(): Flow<List<ChatEntity>> {
        return chatDao.observeArchivedChats()
    }

    fun observeChat(chatId: String): Flow<ChatEntity?> {
        return chatDao.observeChat(chatId)
    }

    fun observeMessages(
        chatId: String
    ): Flow<List<MessageEntity>> {
        return messageDao.observeMessages(chatId)
    }

    fun searchChats(query: String): Flow<List<ChatEntity>> {
        return chatDao.searchChats(query.trim())
    }

    suspend fun createChatWithContact(
        contact: DeviceContact
    ): ChatEntity {
        return database.withTransaction {
            val chatId = "contact_${contact.id}"
            val existing = chatDao.getChatById(chatId)

            if (existing != null) {
                existing
            } else {
                val chat = ChatEntity(
                    id = chatId,
                    contactId = contact.id,
                    userName = contact.name,
                    avatarColor = generateAvatarColor(contact.name)
                )

                chatDao.insertChat(chat)

                chatDao.getChatById(chatId)
                    ?: error("Не удалось создать чат")
            }
        }
    }

    suspend fun sendMessage(
        chatId: String,
        text: String
    ) {
        val trimmedText = text.trim()

        if (trimmedText.isEmpty()) {
            return
        }

        database.withTransaction {
            requireNotNull(chatDao.getChatById(chatId)) {
                "Чат не найден: $chatId"
            }

            val now = System.currentTimeMillis()

            val message = MessageEntity(
                id = UUID.randomUUID().toString(),
                chatId = chatId,
                text = trimmedText,
                isMine = true,
                time = formatTime(now),
                createdAt = now
            )

            messageDao.insertMessage(message)
            refreshPreview(chatId)
        }
    }

    suspend fun editMessage(
        messageId: String,
        newText: String
    ) {
        val trimmedText = newText.trim()

        if (trimmedText.isEmpty()) {
            return
        }

        database.withTransaction {
            val message = messageDao.getMessageById(messageId)

            if (message != null && message.isMine) {
                messageDao.editMessage(messageId, trimmedText)
                refreshPreview(message.chatId)
            }
        }
    }

    suspend fun deleteMessage(messageId: String) {
        database.withTransaction {
            val message = messageDao.getMessageById(messageId)

            if (message != null) {
                messageDao.deleteMessage(messageId)
                refreshPreview(message.chatId)
            }
        }
    }

    suspend fun clearChat(chatId: String) {
        database.withTransaction {
            messageDao.clearChat(chatId)
            refreshPreview(chatId)
            chatDao.markAsRead(chatId)
        }
    }

    suspend fun setChatPinned(
        chatId: String,
        pinned: Boolean
    ) {
        chatDao.setPinned(chatId, pinned)
    }

    suspend fun setChatArchived(
        chatId: String,
        archived: Boolean
    ) {
        chatDao.setArchived(chatId, archived)
    }

    suspend fun markChatAsRead(chatId: String) {
        chatDao.markAsRead(chatId)
    }

    suspend fun deleteChat(chatId: String) {
        chatDao.deleteChat(chatId)
    }

    private suspend fun refreshPreview(chatId: String) {
        val chat = chatDao.getChatById(chatId) ?: return
        val lastMessage = messageDao.getLastMessage(chatId)

        chatDao.updatePreview(
            chatId = chatId,
            text = lastMessage?.text.orEmpty(),
            time = lastMessage?.time.orEmpty(),
            updatedAt = lastMessage?.createdAt ?: chat.updatedAt
        )
    }

    private fun formatTime(timestamp: Long): String {
        return SimpleDateFormat(
            "HH:mm",
            Locale.getDefault()
        ).format(Date(timestamp))
    }

    private fun generateAvatarColor(name: String): Long {
        val colors = listOf(
            0xFF4EC9B0L,
            0xFF7C8CF6L,
            0xFFB58CF6L,
            0xFF5FA8D3L,
            0xFF8B7CF6L
        )

        val index = (name.hashCode() and Int.MAX_VALUE) % colors.size

        return colors[index]
    }
}