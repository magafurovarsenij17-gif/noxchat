package com.noxchat.app.data

data class NoxUser(
    val id: String,
    val name: String,
    val avatarColor: Long
)

data class Message(
    val id: String,
    val text: String,
    val isMine: Boolean,
    val time: String
)

data class Chat(
    val id: String,
    val user: NoxUser,
    val lastMessage: String,
    val lastMessageTime: String,
    val unreadCount: Int,
    val messages: List<Message>
)

object DemoData {

    val currentUser = NoxUser(
        id = "me",
        name = "Вы",
        avatarColor = 0xFF8B7CF6
    )

    private val alice = NoxUser(
        id = "alice",
        name = "Алиса",
        avatarColor = 0xFF4EC9B0
    )

    private val max = NoxUser(
        id = "max",
        name = "Максим",
        avatarColor = 0xFF7C8CF6
    )

    private val sofia = NoxUser(
        id = "sofia",
        name = "София",
        avatarColor = 0xFFB58CF6
    )

    val chats = listOf(
        Chat(
            id = "chat_alice",
            user = alice,
            lastMessage = "Посмотрим новый макет вечером?",
            lastMessageTime = "22:41",
            unreadCount = 2,
            messages = listOf(
                Message(
                    id = "m1",
                    text = "Привет! Как продвигается NoxChat?",
                    isMine = false,
                    time = "22:32"
                ),
                Message(
                    id = "m2",
                    text = "Сделал первый экран и тёмную тему.",
                    isMine = true,
                    time = "22:36"
                ),
                Message(
                    id = "m3",
                    text = "Посмотрим новый макет вечером?",
                    isMine = false,
                    time = "22:41"
                )
            )
        ),
        Chat(
            id = "chat_max",
            user = max,
            lastMessage = "Скинь потом ссылку на дизайн-систему",
            lastMessageTime = "20:18",
            unreadCount = 0,
            messages = listOf(
                Message(
                    id = "m4",
                    text = "Готов помочь с тестированием.",
                    isMine = false,
                    time = "20:10"
                ),
                Message(
                    id = "m5",
                    text = "Спасибо, скоро пришлю сборку.",
                    isMine = true,
                    time = "20:15"
                ),
                Message(
                    id = "m6",
                    text = "Скинь потом ссылку на дизайн-систему",
                    isMine = false,
                    time = "20:18"
                )
            )
        ),
        Chat(
            id = "chat_sofia",
            user = sofia,
            lastMessage = "Ты сегодня свободен?",
            lastMessageTime = "18:03",
            unreadCount = 1,
            messages = listOf(
                Message(
                    id = "m7",
                    text = "Ты сегодня свободен?",
                    isMine = false,
                    time = "18:03"
                )
            )
        )
    )

    val contacts = listOf(
        alice,
        max,
        sofia
    )
}