package ru.etysoft.clientbook.ui.components

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.appcompat.app.AlertDialog
import ru.etysoft.clientbook.R

class AppAlertDialog(context: Context, private val onYesClicked: OnYesClicked) {
    private val dialog: Dialog

    init {
        dialog = Dialog(context)
        dialog.setContentView(R.layout.alert_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun show() {
        dialog.show()

        val dialogView = dialog.window!!.decorView
        val positiveButton = dialogView.findViewById<View>(R.id.dialog_positive_button)
        val negativeButton = dialogView.findViewById<View>(R.id.dialog_negative_button)

        positiveButton.setOnClickListener {
            onYesClicked()
            dialog.dismiss()
        }

        negativeButton.setOnClickListener {
            dialog.dismiss()
        }
    }
}

typealias OnYesClicked = () -> Unit
