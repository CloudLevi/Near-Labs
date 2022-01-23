package com.cloudlevi.nearlabs.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.cloudlevi.nearlabs.R

class ProgressView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint().also {
        it.color = ContextCompat.getColor(context, R.color.blue_1)
        it.style = Paint.Style.FILL
    }

    var stageCount = 3f
    var currentStage = 1f

    fun changeProgress(newStage: Float){
        val animator = ValueAnimator.ofFloat(currentStage, newStage).setDuration(100L)
        animator.addUpdateListener {
            val value = (it.animatedValue as Float)
            currentStage = value
            invalidate()
        }
        animator.start()
    }

    override fun onDraw(canvas: Canvas?) {
        val rect = RectF(0f, 0f, progressX(), maxY())
        canvas?.drawRect(rect, paint)
        super.onDraw(canvas)
    }

    private fun maxX() = width.toFloat()
    private fun maxY() = height.toFloat()
    private fun progressX() = maxX() * (currentStage/stageCount)
    private fun calculateProgressX(stage: Float) = maxX() * (stage/stageCount)
}