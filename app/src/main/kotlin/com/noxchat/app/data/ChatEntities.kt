package com.noxchat.app.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey
    val id: String,

    val contactId: Long? = null,
    val userName: String,
    val avatarColor: Long,

    val lastMessage: String = "",
    val lastMessageTime: String = "",

    val unreadCount: Int = 0,
    val isPinned: Boolean = false,
    val isArchived: Boolean = false,

    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = ChatEntity::class,
            parentColumns = ["id"],
            childColumns = ["chatId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["chatId", "createdAt"])
    ]
)
data class MessageEntity(
    @PrimaryKey
    val id: String,

    val chatId: String,
    val text: String,
    val isMine: Boolean,
    val time: String,
    val createdAt: Long,

    val isEdited: Boolean = false
)