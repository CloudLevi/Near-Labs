package com.cloudlevi.nearlabs.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import com.cloudlevi.nearlabs.R
import com.cloudlevi.nearlabs.core.BaseFragment
import com.cloudlevi.nearlabs.databinding.FragmentCreateAccountBinding
import com.cloudlevi.nearlabs.extensions.makeGone
import com.cloudlevi.nearlabs.extensions.makeVisible
import com.cloudlevi.nearlabs.extensions.showAlertDialog
import com.cloudlevi.nearlabs.ui.login.LoginViewModel.ActionType.*
import com.cloudlevi.nearlabs.ui.login.LoginViewModel.Action

class CreateAccountFragment :
    BaseFragment<FragmentCreateAccountBinding>(R.layout.fragment_create_account) {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCreateAccountBinding =
        FragmentCreateAccountBinding::inflate

    private lateinit var parentFragment: LoginFragment
    private lateinit var viewModel: LoginViewModel

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

        binding.apply {
            fullNameInput.setOnFocusChangeListener { view, b ->
                changeTVFocus(b, fullNameHint)
            }

            walletIDInput.setOnFocusChangeListener { view, b ->
                changeTVFocus(b, walletInfoHint)
                changeIVFocus(b, walletInfoIV)
            }

            createAnAccountBtn.setOnClickListener {
                onCreateAccountClick()
            }

            walletInfoIV.setOnClickListener {
                showAlertDialog(
                    requireContext(),
                    R.string.wallet_id,
                    R.string.wallet_id_description,
                    R.string.ok
                )
            }
        }
    }

    private fun onCreateAccountClick() {
        binding.apply {
            var isError = false
            if (fullNameInput.text.isNullOrEmpty()) {
                isError = true
                fullNameError.makeVisible()
            } else fullNameError.makeGone()

            if (walletIDInput.text.isNullOrEmpty()) {
                isError = true
                walletIDError.makeVisible()
            } else walletIDError.makeGone()

            if (!isError) sendLongToast("Passed")
        }
    }

    private fun doAction(a: Action) {
        when (a.type) {
            else -> {}
        }
    }

    companion object {
        fun getInstance(bundle: Bundle = bundleOf()): CreateAccountFragment {
            val fragment = CreateAccountFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}