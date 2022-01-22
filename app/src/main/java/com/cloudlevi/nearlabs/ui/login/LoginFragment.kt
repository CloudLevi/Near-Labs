package com.cloudlevi.nearlabs.ui.login

import android.os.Bundle
import android.text.InputType
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.cloudlevi.nearlabs.R
import com.cloudlevi.nearlabs.core.BaseFragment
import com.cloudlevi.nearlabs.databinding.FragmentLoginBinding
import com.cloudlevi.nearlabs.enums.SignUpType
import dagger.hilt.android.AndroidEntryPoint
import android.text.style.URLSpan

import android.text.SpannableString

import android.text.Spannable

import android.widget.TextView
import com.cloudlevi.nearlabs.extensions.*
import kotlin.math.sin


@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    private val viewModel: LoginViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLoginBinding =
        FragmentLoginBinding::inflate

    private lateinit var signUpTextWatcher: SignUpTextWatcher

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            signUpTextWatcher = SignUpTextWatcher(signUpInput, viewModel::signUpTextChanged)
            signUpInput.addTextChangedListener(signUpTextWatcher)
            signUpOptionChanged(viewModel.currentSignUpType)

            signUpTypeRG.setOnCheckedChangeListener { _, i ->
                when (i) {
                    R.id.emailOption -> signUpOptionChanged(SignUpType.EMAIL)
                    R.id.phoneOption -> signUpOptionChanged(SignUpType.PHONE)
                }
            }

            signUpBtn.setOnClickListener { onSignUpClicked() }
            loginBtn.setOnClickListener { onLoginClicked() }

            footerTV.movementMethod = LinkMovementMethod.getInstance()
            removeUnderlines(footerTV)
        }
    }

    private fun onSignUpClicked() {
        binding.apply {
            if (isSignUpInputValid(viewModel.currentSignUpType, signUpInput.text.toString())){
                signUpFieldError.makeGone()
                //navigate to register
            } else {
                signUpFieldError.makeVisible()
            }
        }
    }

    private fun onLoginClicked() {
        binding.apply {
            if (walletInput.text.toString().trim().isNotEmpty()){
                nearWalletError.makeGone()
                //navigate to login
            } else {
                nearWalletError.makeVisible()
            }
        }
    }

    private fun removeUnderlines(textView: TextView) {
        val s: Spannable = SpannableString(textView.text)
        val spans = s.getSpans(0, s.length, URLSpan::class.java)
        spans.forEach { span ->
            val start = s.getSpanStart(span)
            val end = s.getSpanEnd(span)
            s.removeSpan(span)
            val newSpan = URLSpanNoUnderline(span.url)
            s.setSpan(newSpan, start, end, 0)
        }
        textView.text = s
    }

    private fun signUpOptionChanged(option: SignUpType) {
        viewModel.currentSignUpType = option
        signUpTextWatcher.signUpType = option
        binding.apply {
            signUpInput.setText(viewModel.getCurrentSignUpText())
            signUpInput.setSelection(signUpInput.text.toString().length)
            when (option) {
                SignUpType.EMAIL -> {
                    signUpInput.setHint(R.string.email_address)
                    signUpInput.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                }
                SignUpType.PHONE -> {
                    signUpInput.setHint(R.string.phone_example)
                    signUpInput.inputType = InputType.TYPE_CLASS_PHONE
                }
            }
        }
    }
}