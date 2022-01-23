package com.cloudlevi.nearlabs.extensions

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.forEach
import com.cloudlevi.nearlabs.enums.SignUpType

val bannedPhoneChars = listOf('.', ',', ' ', '-', '+', '=')

fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.makeGone() {
    this.visibility = View.GONE
}

class MaxLengthListener(
    private var editText: EditText,
    private val callback: (newChar: String?) -> Unit,
    private var maxLength: Int = 1
) : TextWatcher {
    
    private var beforeText = ""

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        beforeText = p0.toString()
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(p0: Editable?) {
        editText.removeTextChangedListener(this)
        val input = p0.toString().trim()
        var newText = input
        if (input.isNotEmpty()) newText = input[0].toString()

        val newCharInt = if (editText.selectionStart == 1) -1 else 1
        if (input.length >= maxLength) callback(input.getOrNull(newCharInt)?.toString())
        editText.setText(newText)
        editText.setSelection(newText.length)
        editText.addTextChangedListener(this)
    }

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
        if (input.isEmpty()) {
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
            "${output.subStringSafe(0, 5)} ${output.subStringSafe(5, 8)} ${
                output.subStringSafe(
                    8,
                    12
                )
            }".trimEnd()


        editText.setText(outputSpaced)
        if (selectionStart > (outputSpaced.length - 1)) selectionStart = outputSpaced.length
        else if (outputSpaced.length > selectionStart) selectionStart = outputSpaced.length
        editText.setSelection(selectionStart)

        editText.addTextChangedListener(this)

        callback(outputSpaced)
    }
}

fun EditText.onSingleCharDeleteListener(focusTo: EditText?, int: Int) {
    this.setOnKeyListener { _, code, e ->
        if(e.action == KeyEvent.ACTION_UP)
            return@setOnKeyListener false
        if (code == KeyEvent.KEYCODE_DEL){
            focusTo?.requestFocusAtEnd()
        }
        return@setOnKeyListener false
    }
}

fun EditText.requestFocusAtStart(){
    setSelection(0)
    requestFocus()
}

fun EditText.requestFocusAtEnd(withChar: String? = null){
    if (withChar != null) setText(withChar)
    setSelection(text.toString().length)
    requestFocus()
}

fun toggleAllViewsEnabled(isEnabled: Boolean, view: View) {
    if (view is ViewGroup) {
        view.forEach {
            toggleAllViewsEnabled(isEnabled, it)
        }
    } else view.isEnabled = isEnabled
}