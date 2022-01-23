package com.cloudlevi.nearlabs.view

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import android.view.WindowInsets

class CustomRelativeLayout(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    private val mInsets = IntArray(4)

    override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
        mInsets[0] = insets.systemWindowInsetLeft
        mInsets[1] = insets.systemWindowInsetTop
        mInsets[2] = insets.systemWindowInsetRight
        return super.onApplyWindowInsets(
            insets.replaceSystemWindowInsets(
                0, 0, 0,
                insets.systemWindowInsetBottom
            )
        )
    }
}