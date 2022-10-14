package com.ebenezer.gana.fcsibbul.utils

import android.app.Activity
import android.net.Uri
import android.webkit.MimeTypeMap

object Constants {

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

    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        /**
         * MimeTypeMap: Two-way map that maps MIME-types to file extensions and vice versa
         *
         * getSingleton(): Get the singleton instance of MimeTypeMap.
         *
         * getExtensionFromMimeType:Return the registered extension for the given Mime Type
         *
         * contentResolver.getType: Returns the MIME type of the given content URL.
         */


        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}