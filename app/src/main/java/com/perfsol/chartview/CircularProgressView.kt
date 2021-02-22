package com.perfsol.chartview

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.FloatRange
import kotlin.math.min

class CircularProgressView @JvmOverloads constructor(
	context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
	
	// The paint for the progress track (the background of the ring)
	private val backgroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
		style = Paint.Style.STROKE
	}
	
	private val progressPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
		style = Paint.Style.STROKE
	}
	
	private val rect = RectF()
	private val startAngle = -90f
	private val maxAngle = 360f
	private val maxProgress = 100
	private var progressWidth = 16.px.toFloat()
	
	private var diameter = 0f
	private var angle = 0f
	
	init {
		val array = context.obtainStyledAttributes(
			attrs, R.styleable.CircularProgressView, defStyleAttr, 0
		)
		val backgroundColor = array.getColor(
			R.styleable.CircularProgressView_background_color,
			Color.parseColor("#F5F5F5")
		)
		val progressColor = array.getColor(
			R.styleable.CircularProgressView_ring_color,
			Color.parseColor("#f44336")
		)
		array.recycle()
		
		with(backgroundPaint) {
			color = backgroundColor
			strokeWidth = progressWidth
		}
		
		with(progressPaint) {
			color = progressColor
			strokeCap = Paint.Cap.ROUND
			strokeWidth = progressWidth
		}
	}
	
	override fun onDraw(canvas: Canvas) {
		drawCircle(maxAngle, canvas, backgroundPaint)
		drawCircle(angle, canvas, progressPaint)
	}
	
	override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
		diameter = min(width, height).toFloat()
		updateRect()
	}
	
	private fun updateRect() {
		val strokeWidth = backgroundPaint.strokeWidth
		rect.set(strokeWidth, strokeWidth, diameter - strokeWidth, diameter - strokeWidth)
	}
	
	private fun drawCircle(angle: Float, canvas: Canvas, paint: Paint) {
		canvas.drawArc(rect, startAngle, angle, false, paint)
	}
	
	private fun calculateAngle(progress: Float) = maxAngle / maxProgress * progress
	
	fun setProgress(@FloatRange(from = 0.0, to = 100.0) progress: Float) {
		angle = calculateAngle(progress)
		invalidate()
	}
	
	fun setProgressColor(color: Int) {
		progressPaint.color = color
		invalidate()
	}
	
	fun setProgressBackgroundColor(color: Int) {
		backgroundPaint.color = color
		invalidate()
	}
	
	fun setProgressWidth(width: Float) {
		progressPaint.strokeWidth = width
		backgroundPaint.strokeWidth = width
		updateRect()
		invalidate()
	}
	
	fun setRounded(rounded: Boolean) {
		progressPaint.strokeCap = if (rounded) Paint.Cap.ROUND else Paint.Cap.BUTT
		invalidate()
	}
	
	private val Int.px: Int
		get() = (this * Resources.getSystem().displayMetrics.density).toInt()
}