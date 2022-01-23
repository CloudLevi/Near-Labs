package com.cloudlevi.nearlabs.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cloudlevi.nearlabs.ui.login.CreateAccountFragment
import com.cloudlevi.nearlabs.ui.login.LoginFragment
import com.cloudlevi.nearlabs.ui.login.VerificationFragment

class SignUpViewPagerAdapter(
    rootFragment: LoginFragment,
    private val pageCount: Int
) : FragmentStateAdapter(rootFragment) {

    override fun getItemCount() = pageCount

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> VerificationFragment.getInstance()
            1 -> CreateAccountFragment.getInstance()
            else -> VerificationFragment.getInstance()
        }
    }
}