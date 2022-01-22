package com.cloudlevi.nearlabs.extensions

import android.text.TextUtils
import android.util.Patterns
import com.cloudlevi.nearlabs.enums.SignUpType
import kotlin.math.sin

fun isSignUpInputValid(signUpType: SignUpType, text: String): Boolean {
    val input = text.trim()
    return when(signUpType){
        SignUpType.EMAIL -> {
            !TextUtils.isEmpty(input) && Patterns.EMAIL_ADDRESS.matcher(input).matches()
        }
        SignUpType.PHONE -> {
            val phoneDigits = input.filter { it.isDigit() }
            phoneDigits.length == 10
        }
    }
}