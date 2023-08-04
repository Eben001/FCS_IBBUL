package com.ebenezer.gana.fcsibbul.utils

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import java.util.*

object Constants {

    const val PAGE_SIZE = 10
    const val WELCOME_SCREEN_IMAGES: String = "welcome_screen_images"
    const val EXCOS_IMAGE: String = "excos_image"
    const val DRAWER_STATE_LOCKED_CLOSED = 1
    const val DRAWER_STATE_UNLOCKED = 0
    const val PICK_IMAGE_REQUEST_CODE = 1
    const val READ_STORAGE_PERMISSION_CODE = 2

    const val ANNOUNCEMENTS: String = "announcements"
    const val EXCOS: String = "excos"
    const val USERS: String = "users"
    const val SONG: String = "song"
    const val BIBLE_VERSE: String = "daily_verse"

    fun getFileExtension(imageFileURI: Uri?): String? {
        imageFileURI?.let { uri ->
            val fileName = uri.lastPathSegment
            val dotIndex = fileName?.lastIndexOf(".")
            if (dotIndex != null && dotIndex >= 0) {
                return fileName.substring(dotIndex + 1)
            }
        }
        return null
    }

    fun getFileExtension(context:Context, imageUri: Uri?): String? {
        val contentResolver = context.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()

        // Get the file type based on the image's URI
        val type = imageUri?.let { contentResolver.getType(it) }

        // If we were unable to determine the file type based on the URI, try to guess based on the file extension
        if (type == null) {
            val extension = mimeTypeMap.getExtensionFromMimeType(imageUri?.let {
                contentResolver.getType(
                    it
                )
            })
            return extension?.lowercase(Locale.ROOT)
        }

        // Return the file extension in lowercase
        return mimeTypeMap.getExtensionFromMimeType(type)?.lowercase(Locale.ROOT)
    }
}