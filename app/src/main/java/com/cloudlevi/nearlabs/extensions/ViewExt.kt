package com.cloudlevi.nearlabs.extensions

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.cloudlevi.nearlabs.enums.SignUpType


val bannedPhoneChars = listOf('.', ',', ' ', '-', '+', '=')
val allowedPhoneChars = listOf('(', ')')

fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.makeGone() {
    this.visibility = View.GONE
}

fun EditText.addPhoneWatcher(onTextChanged: (String) -> Unit) {
    val watcher = SignUpTextWatcher(this, onTextChanged)
    this.addTextChangedListener(watcher)
}

class SignUpTextWatcher(
    private val editText: EditText,
    private val callback: (String) -> Unit
) : TextWatcher {

    var signUpType = SignUpType.EMAIL

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(p0: Editable?) {
        if (signUpType == SignUpType.EMAIL) {
            callback(p0.toString())
            return
        }

        val input = p0.toString().trim().filter {
            !bannedPhoneChars.contains(it) && it.isDigit()
        }
        if (input.isEmpty()){
            editText.removeTextChangedListener(this)
            editText.setText("")
            editText.addTextChangedListener(this)
            return
        }

        var output = input

        editText.removeTextChangedListener(this)

        var selectionStart = editText.selectionStart

        if (input[0] != '(') output = "($output"
        if (input.length > 3 && input[3] != ')') {
            output = "${output.substring(0, 4)})${input.substring(3, input.length)}"
        }

        val outputSpaced =
            "${output.subStringSafe(0, 5)} ${output.subStringSafe(5, 8)} ${output.subStringSafe(8, 12)}".trimEnd()


        editText.setText(outputSpaced)
        if (selectionStart > (outputSpaced.length - 1)) selectionStart = outputSpaced.length
        else if (outputSpaced.length > selectionStart) selectionStart = outputSpaced.length
        editText.setSelection(selectionStart)

        editText.addTextChangedListener(this)

        callback(outputSpaced)
    }
}

fun String.subStringSafe(start: Int, end: Int): String {
    return if (this.length > start && end <= this.length) substring(start, end)
    else if (this.length > start) substring(start, this.length)
    else ""
}