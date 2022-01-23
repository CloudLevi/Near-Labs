package com.cloudlevi.nearlabs.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.widget.NestedScrollView

class LockableNestedScrollView(context: Context, attrs: AttributeSet): NestedScrollView(context, attrs) {

    var swipeEnabled = true

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return if (swipeEnabled) {
            super.onInterceptTouchEvent(ev)
        } else {
            false
        }
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return if (swipeEnabled) {
            super.onTouchEvent(ev)
        } else {
            false
        }
    }
}