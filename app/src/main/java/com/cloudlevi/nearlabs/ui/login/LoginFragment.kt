package com.cloudlevi.nearlabs.ui.login

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.text.method.LinkMovementMethod
import androidx.fragment.app.viewModels
import com.cloudlevi.nearlabs.R
import com.cloudlevi.nearlabs.core.BaseFragment
import com.cloudlevi.nearlabs.databinding.FragmentLoginBinding
import com.cloudlevi.nearlabs.enums.SignUpType
import dagger.hilt.android.AndroidEntryPoint
import android.text.style.URLSpan
import android.text.SpannableString
import android.text.Spannable
import android.view.*
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.cloudlevi.nearlabs.extensions.*
import com.cloudlevi.nearlabs.ui.adapters.SignUpViewPagerAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.cloudlevi.nearlabs.ui.login.LoginViewModel.ActionType.*

const val SIGN_UP_PAGE_COUNT = 3

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    private val viewModel: LoginViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLoginBinding =
        FragmentLoginBinding::inflate

    private lateinit var signUpTextWatcher: SignUpTextWatcher
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<RelativeLayout>

    private var isGetStartedActive = false

    private val signUpPagerAdapter: SignUpViewPagerAdapter by lazy {
        SignUpViewPagerAdapter(this, SIGN_UP_PAGE_COUNT)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        viewModel.action.observe(viewLifecycleOwner) {
            val data = it.peekData()
            doAction(data)
        }
        return binding.root
    }

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

            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet.root)

            footerTV.movementMethod = LinkMovementMethod.getInstance()
            removeUnderlines(footerTV)

            bottomSheet.apply {
                dismissBtn.setOnClickListener { hideRegisterSheet() }

                signUpViewPager.isUserInputEnabled = false
                signUpViewPager.adapter = signUpPagerAdapter
                signUpViewPager.offscreenPageLimit = 3
            }
        }
    }

    private fun onSignUpClicked() {
        binding.apply {
            if (isSignUpInputValid(viewModel.currentSignUpType, signUpInput.text.toString())) {
                signUpFieldError.makeGone()
                openRegisterSheet()
            } else {
                signUpFieldError.makeVisible()
            }
        }
    }

    private fun onLoginClicked() {
        binding.apply {
            if (walletInput.text.toString().trim().isNotEmpty()) {
                nearWalletError.makeGone()
                //navigate to login
            } else {
                nearWalletError.makeVisible()
            }
        }
    }

    private fun openRegisterSheet() {
        toggleAllViewsEnabled(false, binding.rootScrollView)
        binding.rootScrollView.swipeEnabled = false
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        binding.bottomSheet.progressView.invalidate()
        animateScrollForeground(true)
    }

    private fun hideRegisterSheet() {
        toggleAllViewsEnabled(true, binding.rootScrollView)
        animateScrollForeground(false)
        binding.rootScrollView.swipeEnabled = true
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun animateScrollForeground(isDim: Boolean) {
        val startColor: Int
        val endColor: Int
        if (isDim) {
            startColor = ContextCompat.getColor(requireContext(), android.R.color.transparent)
            endColor = ContextCompat.getColor(requireContext(), R.color.black_transparent_80)
        } else {
            startColor = ContextCompat.getColor(requireContext(), R.color.black_transparent_80)
            endColor = ContextCompat.getColor(requireContext(), android.R.color.transparent)
        }

        ObjectAnimator.ofObject(ArgbEvaluator(), startColor, endColor).also {
            it.duration = 150
            it.addUpdateListener { vA ->
                binding.rootScrollView.foreground = ColorDrawable(vA.animatedValue as Int)
            }
            it.start()
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

    private fun doAction(a: LoginViewModel.Action) {
        when (a.type) {
            CODE_VERIFICATION_SUCCESS -> onCodeVerificationSuccess()
            GET_STARTED_VALID -> onGetStartedValid(a.bool?: false)
            else -> {}
        }
    }

    private fun onGetStartedValid(isValid: Boolean){
        if (isGetStartedActive != isValid){
            isGetStartedActive = isValid
            animateActivationChange(binding.signUpBtn, isGetStartedActive)
        }
    }

    private fun onCodeVerificationSuccess() {
        binding.apply {
            bottomSheet.apply {
                hideKeyboard(requireActivity().currentFocus)
                progressView.changeProgress(2f)
                signUpViewPager.setCurrentItem(1, true)
                headerTV.text = getString(R.string.create_near_account)
            }
        }
    }

    fun onSendToDifferentEmailClick() {
        hideKeyboard(requireActivity().currentFocus)
        binding.emailOption.isChecked = true
        binding.signUpInput.setText("")
        hideRegisterSheet()
        binding.signUpInput.requestFocus()
        showKeyboard(binding.signUpInput)
    }
}