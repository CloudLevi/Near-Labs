package com.cloudlevi.nearlabs.core

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.cloudlevi.nearlabs.R

abstract class BaseFragment<VB : ViewBinding>(@LayoutRes layoutID: Int) : Fragment(layoutID) {

    protected lateinit var binding: VB

    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = bindingInflater.invoke(inflater, container, false)
        return binding.root
    }

    protected fun hideKeyboard(view: View?) {
        val iMM =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        iMM.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    protected fun showKeyboard(view: View) {
        val iMM =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        iMM.showSoftInput(view, 0)
    }

    protected fun sendShortToast(string: String) =
        Toast.makeText(requireContext(), string, Toast.LENGTH_SHORT).show()

    protected fun sendShortToast(@StringRes id: Int) =
        Toast.makeText(requireContext(), id, Toast.LENGTH_SHORT).show()

    protected fun sendLongToast(string: String) =
        Toast.makeText(requireContext(), string, Toast.LENGTH_LONG).show()

    protected fun sendLongToast(@StringRes id: Int) =
        Toast.makeText(requireContext(), id, Toast.LENGTH_LONG).show()

    protected fun changeTVFocus(
        isFocused: Boolean,
        textView: TextView,
        @ColorRes defaultColor: Int = R.color.gray_3
    ) {
        val startColor = textView.currentTextColor
        val endColor = ContextCompat.getColor(
            requireContext(),
            (if (isFocused) R.color.blue_1 else defaultColor)
        )
        ValueAnimator.ofArgb(startColor, endColor).also {
            it.duration = 100
            it.addUpdateListener { vA ->
                textView.setTextColor(vA.animatedValue as Int)
            }
            it.start()
        }
    }

    protected fun changeIVFocus(
        isFocused: Boolean,
        imageView: ImageView,
        @ColorRes defaultColor: Int = R.color.gray_3
    ) {
        val startColor = ContextCompat.getColor(
            requireContext(),
            (if (isFocused) defaultColor else R.color.blue_1)
        )
        val endColor = ContextCompat.getColor(
            requireContext(),
            (if (isFocused) R.color.blue_1 else defaultColor)
        )
        ValueAnimator.ofArgb(startColor, endColor).also {
            it.duration = 100
            it.addUpdateListener { vA ->
                imageView.setColorFilter(vA.animatedValue as Int)
            }
            it.start()
        }
    }

    protected fun animateActivationChange(button: View, isActivated: Boolean) {
        val startColor = ContextCompat.getColor(
            requireContext(),
            if (isActivated) R.color.gray_4 else R.color.black
        )
        val endColor = ContextCompat.getColor(
            requireContext(),
            if (isActivated) R.color.black else R.color.gray_4
        )

        ObjectAnimator.ofObject(ArgbEvaluator(), startColor, endColor).also {
            it.duration = 150
            it.addUpdateListener { vA ->
                ViewCompat.setBackgroundTintList(
                    button,
                    ColorStateList.valueOf(vA.animatedValue as Int)
                )
            }
            it.start()
        }
    }
}