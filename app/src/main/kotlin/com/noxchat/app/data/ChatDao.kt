package com.noxchat.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Query(
        """
        SELECT * FROM chats
        WHERE isArchived = 0
        ORDER BY isPinned DESC, updatedAt DESC, id ASC
        """
    )
    fun observeActiveChats(): Flow<List<ChatEntity>>

    @Query(
        """
        SELECT * FROM chats
        WHERE isArchived = 1
        ORDER BY isPinned DESC, updatedAt DESC, id ASC
        """
    )
    fun observeArchivedChats(): Flow<List<ChatEntity>>

    @Query(
        """
        SELECT * FROM chats
        WHERE id = :chatId
        LIMIT 1
        """
    )
    fun observeChat(chatId: String): Flow<ChatEntity?>

    @Query(
        """
        SELECT * FROM chats
        WHERE id = :chatId
        LIMIT 1
        """
    )
    suspend fun getChatById(chatId: String): ChatEntity?

    @Query(
        """
        SELECT * FROM chats
        WHERE isArchived = 0
        AND (
            instr(lower(userName), lower(:query)) > 0
            OR instr(lower(lastMessage), lower(:query)) > 0
        )
        ORDER BY isPinned DESC, updatedAt DESC, id ASC
        """
    )
    fun searchChats(query: String): Flow<List<ChatEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertChat(chat: ChatEntity)

    @Query(
        """
        UPDATE chats
        SET lastMessage = :text,
            lastMessageTime = :time,
            updatedAt = :updatedAt
        WHERE id = :chatId
        """
    )
    suspend fun updatePreview(
        chatId: String,
        text: String,
        time: String,
        updatedAt: Long
    )

    @Query(
        """
        UPDATE chats
        SET isPinned = :pinned
        WHERE id = :chatId
        """
    )
    suspend fun setPinned(
        chatId: String,
        pinned: Boolean
    )

    @Query(
        """
        UPDATE chats
        SET isArchived = :archived
        WHERE id = :chatId
        """
    )
    suspend fun setArchived(
        chatId: String,
        archived: Boolean
    )

    @Query(
        """
        UPDATE chats
        SET unreadCount = 0
        WHERE id = :chatId
        """
    )
    suspend fun markAsRead(chatId: String)

    @Query("DELETE FROM chats WHERE id = :chatId")
    suspend fun deleteChat(chatId: String)
}