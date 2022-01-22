package com.cloudlevi.nearlabs.ui.login

import androidx.lifecycle.ViewModel
import com.cloudlevi.nearlabs.enums.SignUpType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    var currentSignUpType = SignUpType.EMAIL
    var emailText: String = ""
    var phoneText: String = ""

    fun signUpTextChanged(text: String) {
        when (currentSignUpType) {
            SignUpType.EMAIL -> emailText = text.trim()
            SignUpType.PHONE -> phoneText = text.trim()
        }
    }

    fun getCurrentSignUpText(): String {
        return when (currentSignUpType) {
            SignUpType.EMAIL -> emailText
            SignUpType.PHONE -> phoneText
        }
    }
}