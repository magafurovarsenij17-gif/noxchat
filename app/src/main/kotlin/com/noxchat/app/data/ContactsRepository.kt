package com.noxchat.app.data

import android.content.Context
import android.provider.ContactsContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class DeviceContact(
    val id: Long,
    val name: String,
    val photoUri: String?
)

class ContactsRepository(
    private val context: Context
) {

    suspend fun loadContacts(): List<DeviceContact> = withContext(Dispatchers.IO) {
        val contacts = mutableListOf<DeviceContact>()

        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
        )

        val selection = "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} IS NOT NULL"
        val sortOrder = "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} ASC"

        context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            projection,
            selection,
            null,
            sortOrder
        )?.use { cursor ->
            val idIndex = cursor.getColumnIndexOrThrow(
                ContactsContract.Contacts._ID
            )

            val nameIndex = cursor.getColumnIndexOrThrow(
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
            )

            val photoIndex = cursor.getColumnIndexOrThrow(
                ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
            )

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idIndex)
                val name = cursor.getString(nameIndex) ?: continue
                val photoUri = cursor.getString(photoIndex)

                contacts.add(
                    DeviceContact(
                        id = id,
                        name = name,
                        photoUri = photoUri
                    )
                )
            }
        }

        contacts
    }
}