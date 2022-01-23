package com.cloudlevi.nearlabs.extensions

import android.content.Context
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.cloudlevi.nearlabs.R
import com.cloudlevi.nearlabs.databinding.DialogInfoBinding

fun showAlertDialog(
    context: Context,
    @StringRes titleID: Int,
    @StringRes descriptionID: Int,
    @StringRes posTextID: Int
) {
    val dialogBuilder = AlertDialog.Builder(
        context,
        R.style.AppTheme_AlertDialogStyle
    )

    val view = View.inflate(context, R.layout.dialog_info, null)
    val binding = DialogInfoBinding.bind(view)

    dialogBuilder.setView(binding.root)

    binding.apply {
        titleTV.text = context.getString(titleID)
        descriptionTV.text = context.getString(descriptionID)

        dialogBuilder.setPositiveButton(context.getString(posTextID)) { _, _ -> }

        val dialog = dialogBuilder.create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener { dialog.dismiss() }
        }

        dialog.show()
        dialog.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.rounded_bg_16dp
            )
        )
    }

}