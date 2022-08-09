package com.ebenezer.gana.fcsibbul.ui.dialogs

import android.content.Context
import com.ebenezer.gana.fcsibbul.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class DialogsNavigator @Inject constructor(@ActivityContext private val context: Context) {

    fun showContactusDialog() {
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.dialog_title)
            .setMessage(R.string.dialog_message)
            .setPositiveButton(R.string.ok_dialog_message) { dialog, _ ->
                dialog.dismiss()
            }.show()

    }

    fun showDonateDialog() {
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.dialog_donate_title)
            .setMessage(R.string.dialog_donate_message)
            .setPositiveButton(R.string.ok_dialog_message) { dialog, _ ->
                dialog.dismiss()
            }.show()

    }

}