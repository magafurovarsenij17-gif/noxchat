package com.noxchat.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Query(
        """
        SELECT * FROM messages
        WHERE chatId = :chatId
        ORDER BY createdAt ASC, id ASC
        """
    )
    fun observeMessages(
        chatId: String
    ): Flow<List<MessageEntity>>

    @Query(
        """
        SELECT * FROM messages
        WHERE id = :messageId
        LIMIT 1
        """
    )
    suspend fun getMessageById(
        messageId: String
    ): MessageEntity?

    @Query(
        """
        SELECT * FROM messages
        WHERE chatId = :chatId
        ORDER BY createdAt DESC, id DESC
        LIMIT 1
        """
    )
    suspend fun getLastMessage(
        chatId: String
    ): MessageEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertMessage(message: MessageEntity)

    @Query(
        """
        UPDATE messages
        SET text = :text,
            isEdited = 1
        WHERE id = :messageId
        """
    )
    suspend fun editMessage(
        messageId: String,
        text: String
    )

    @Query("DELETE FROM messages WHERE id = :messageId")
    suspend fun deleteMessage(messageId: String)

    @Query("DELETE FROM messages WHERE chatId = :chatId")
    suspend fun clearChat(chatId: String)
}