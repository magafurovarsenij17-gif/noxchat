package com.noxchat.app.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

object ChatStore {

    val chats: SnapshotStateList<Chat> = mutableStateListOf()

    init {
        chats.addAll(DemoData.chats)
    }

    fun findChatByContact(contactId: Long): Chat? {
        return chats.firstOrNull { chat ->
            chat.id == "contact_$contactId"
        }
    }

    fun createChatWithContact(contact: DeviceContact): Chat {
        val chatId = "contact_${contact.id}"

        val existingChat = findChatByContact(contact.id)

        if (existingChat != null) {
            return existingChat
        }

        val newChat = Chat(
            id = chatId,
            user = NoxUser(
                id = contact.id.toString(),
                name = contact.name,
                avatarColor = generateAvatarColor(contact.name)
            ),
            lastMessage = "Начните переписку",
            lastMessageTime = "Сейчас",
            unreadCount = 0,
            messages = emptyList()
        )

        chats.add(0, newChat)

        return newChat
    }

    fun sendMessage(
        chatId: String,
        text: String
    ) {
        val trimmedText = text.trim()

        if (trimmedText.isEmpty()) {
            return
        }

        val chat = chats.firstOrNull { it.id == chatId } ?: return

        val message = Message(
            id = System.currentTimeMillis().toString(),
            text = trimmedText,
            isMine = true,
            time = "Сейчас"
        )

        val chatIndex = chats.indexOfFirst { it.id == chatId }

        if (chatIndex != -1) {
            chats[chatIndex] = chat.copy(
                messages = chat.messages + message,
                lastMessage = message.text,
                lastMessageTime = message.time
            )
        }
    }

    private fun generateAvatarColor(name: String): Long {
        val colors = listOf(
            0xFF4EC9B0,
            0xFF7C8CF6,
            0xFFB58CF6,
            0xFF5FA8D3,
            0xFF8B7CF6
        )

        val index = name.hashCode().mod(colors.size)

        return colors[index]
    }
}