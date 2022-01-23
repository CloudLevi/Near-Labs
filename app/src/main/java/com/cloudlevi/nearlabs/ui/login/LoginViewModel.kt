package com.cloudlevi.nearlabs.ui.login

import androidx.lifecycle.ViewModel
import com.cloudlevi.nearlabs.R
import com.cloudlevi.nearlabs.enums.SignUpType
import com.cloudlevi.nearlabs.extensions.ActionLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    var currentSignUpType = SignUpType.EMAIL
    var emailText: String = ""
    var phoneText: String = ""

    private var verificationCode = "000000"

    val action = ActionLiveData<Action>()

    fun signUpTextChanged(text: String) {
        when (currentSignUpType) {
            SignUpType.EMAIL -> emailText = text.trim()
            SignUpType.PHONE -> phoneText = text.trim()
        }
        action.set(Action(ActionType.UPDATE_CHILDREN, R.layout.fragment_verification))
    }

    fun getCurrentSignUpText(): String {
        return when (currentSignUpType) {
            SignUpType.EMAIL -> emailText
            SignUpType.PHONE -> phoneText
        }
    }

    fun onVerificationContinueClick(inputString: String) {
        val actionType = if (inputString == verificationCode) ActionType.CODE_VERIFICATION_SUCCESS
        else ActionType.CODE_VERIFICATION_ERROR
        action.set(Action(actionType))
    }

    enum class ActionType {
        UPDATE_CHILDREN,
        CODE_VERIFICATION_ERROR,
        CODE_VERIFICATION_SUCCESS
    }

    data class Action(val type: ActionType, val childID: Int? = null, val bool: Boolean? = null)
}