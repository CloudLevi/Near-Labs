package com.cloudlevi.nearlabs.ui.login

import androidx.lifecycle.ViewModel
import com.cloudlevi.nearlabs.R
import com.cloudlevi.nearlabs.enums.SignUpType
import com.cloudlevi.nearlabs.extensions.ActionLiveData
import com.cloudlevi.nearlabs.extensions.isSignUpInputValid
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    var currentSignUpType = SignUpType.EMAIL
        set(value) {
            field = value
            checkIfGetStartedValid()
        }
    var emailText: String = ""
    var phoneText: String = ""

    var createAccountFullName: String = ""
        set(value) {
            field = value
            createWalletInputsChanged()
        }
    var createAccountNearWallet: String = ""
        set(value) {
            field = value
            createWalletInputsChanged()
        }

    private var verificationCode = "000000"

    val action = ActionLiveData<Action>()

    fun signUpTextChanged(text: String) {
        when (currentSignUpType) {
            SignUpType.EMAIL -> {
                emailText = text.trim()
            }
            SignUpType.PHONE -> {
                phoneText = text.trim()
            }
        }
        checkIfGetStartedValid()
        action.set(Action(ActionType.UPDATE_CHILDREN, R.layout.fragment_verification))
    }

    private fun checkIfGetStartedValid() {
        val text = when (currentSignUpType) {
            SignUpType.EMAIL -> emailText
            else -> phoneText
        }
        action.set(
            Action(
                ActionType.GET_STARTED_VALID,
                bool = isSignUpInputValid(currentSignUpType, text)
            )
        )
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

    fun afterVerificationCodeInputCheck(inputString: String) {
        action.set(Action(ActionType.VERIFICATION_STYLE_VALID, bool = inputString.length == 6))
    }

    fun checkCreateAccountInputs(): Boolean =
        createAccountFullName.length >= 3 && isCreateNearWalletCorrect()

    private fun createWalletInputsChanged() {
        action.set(
            Action(
                ActionType.IS_CREATE_INPUTS_SUFFICIENT,
                bool = checkCreateAccountInputs()
            )
        )
    }

    fun isCreateNearWalletCorrect(): Boolean =
        createAccountNearWallet.length >= 3 &&
                createAccountNearWallet.first().isLetter() &&
                createAccountNearWallet.last().isLetter() &&
                createAccountNearWallet.contains('.')

    enum class ActionType {
        GET_STARTED_VALID,
        VERIFICATION_STYLE_VALID,
        UPDATE_CHILDREN,
        CODE_VERIFICATION_ERROR,
        CODE_VERIFICATION_SUCCESS,
        IS_CREATE_INPUTS_SUFFICIENT
    }

    data class Action(val type: ActionType, val childID: Int? = null, val bool: Boolean? = null)
}