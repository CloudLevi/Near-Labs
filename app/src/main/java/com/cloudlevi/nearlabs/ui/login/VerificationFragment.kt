package com.cloudlevi.nearlabs.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.cloudlevi.nearlabs.R
import com.cloudlevi.nearlabs.core.BaseFragment
import com.cloudlevi.nearlabs.databinding.FragmentVerificationBinding
import com.cloudlevi.nearlabs.enums.SignUpType
import com.cloudlevi.nearlabs.extensions.*
import com.cloudlevi.nearlabs.ui.login.LoginViewModel.ActionType.*
import com.cloudlevi.nearlabs.ui.login.LoginViewModel.Action

class VerificationFragment :
    BaseFragment<FragmentVerificationBinding>(R.layout.fragment_verification) {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentVerificationBinding =
        FragmentVerificationBinding::inflate

    private lateinit var viewModel: LoginViewModel
    private lateinit var parentFragment: LoginFragment

    private var listOfVerificationInputs = listOf<EditText>()
    private var isVerificationStylValid = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        parentFragment = requireParentFragment() as LoginFragment
        viewModel = ViewModelProvider(parentFragment)[LoginViewModel::class.java]

        viewModel.action.observe(viewLifecycleOwner) {
            val data = it.peekData() ?: return@observe
            doAction(data)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVerificationSentText()

        binding.apply {

            listOfVerificationInputs = listOf(ver1, ver2, ver3, ver4, ver5, ver6)

            listOfVerificationInputs.forEachIndexed { i, eText ->
                eText.addTextChangedListener(MaxLengthListener(eText, { s ->
                    val nextEditText = listOfVerificationInputs.getOrNull(i + 1)
                    if (s != null) {
                        nextEditText?.setText(s)
                        nextEditText?.setSelection(s.length)
                    } else nextEditText?.requestFocusAtStart()
                }, afterInputCallback = {
                    viewModel.afterVerificationCodeInputCheck(collectCodeInput())
                }))
                eText.onSingleCharDeleteListener(listOfVerificationInputs.getOrNull(i - 1))
            }

            sendToDifferentEmail.setOnClickListener {
                listOfVerificationInputs.forEach {
                    it.setText("")
                    it.clearFocus()
                }
                parentFragment.onSendToDifferentEmailClick()
            }

            resendCode.setOnClickListener { sendLongToast(R.string.code_sent) }

            continueBtn.setOnClickListener { onContinueClick() }
        }
    }

    private fun onContinueClick() {
        binding.apply {
            val collectedInput = collectCodeInput()
            if (collectedInput.length < 6) {
                verificationError.setText(R.string.verification_code_too_short)
                verificationError.makeVisible()
            } else {
                verificationError.makeGone()
                viewModel.onVerificationContinueClick(collectedInput)
            }
        }
    }

    private fun collectCodeInput(): String {
        var string = ""
        listOfVerificationInputs.forEach {
            string += it.text.toString()
        }
        return string
    }

    private fun setVerificationSentText() {
        binding.apply {
            val string = when (viewModel.currentSignUpType) {
                SignUpType.PHONE -> getString(R.string.sent_code_phone, viewModel.phoneText)
                SignUpType.EMAIL -> getString(R.string.sent_code_email, viewModel.emailText)
            }

            verificationTV.text = HtmlCompat.fromHtml(string, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

    private fun doAction(a: Action) {
        when (a.type) {
            UPDATE_CHILDREN -> onUpdateChildren(a.childID ?: 0)
            CODE_VERIFICATION_ERROR -> onVerificationResult()
            VERIFICATION_STYLE_VALID -> onVerificationStyleValid(a.bool ?: false)
        }
    }

    private fun onVerificationStyleValid(isValid: Boolean) {
        if (isVerificationStylValid != isValid){
            isVerificationStylValid = isValid
            animateActivationChange(binding.continueBtn, isVerificationStylValid)
        }
    }

    private fun onVerificationResult() {
        binding.verificationError.setText(R.string.verification_code_incorrect)
        binding.verificationError.makeVisible()
    }

    private fun onUpdateChildren(id: Int) {
        if (id == R.layout.fragment_verification) {
            setVerificationSentText()
        }
    }

    companion object {
        fun getInstance(bundle: Bundle = bundleOf()): VerificationFragment {
            val instance = VerificationFragment()
            instance.arguments = bundle
            return instance
        }
    }

}