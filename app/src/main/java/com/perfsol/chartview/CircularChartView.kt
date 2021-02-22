package com.perfsol.chartview

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class CircularChartView @JvmOverloads constructor(
	context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) :	View(context, attrs, defStyleAttr) {
	
	private val rect = RectF()
	private var startAngle = -90f
	private val maxAngle = 360f
	private val maxProgress = 100
	private val defaultStrokeWidth: Float = 16.px.toFloat()
	
	private var progressStrokeWidth = defaultStrokeWidth
	
	/*Pairs of angle and color*/
	private var segments = emptyList<Pair<Float, Int>>()
	
	private val backgroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
		style = Paint.Style.STROKE
		strokeCap = Paint.Cap.BUTT
		color = Color.parseColor("#e4e9ef")
	}
	private val progressPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
		style = Paint.Style.STROKE
		strokeCap = Paint.Cap.BUTT
	}
	
	private var diameter = 0f
	
	init {
		val array = context.obtainStyledAttributes(attrs, R.styleable.CircularChartView, defStyleAttr, 0)
		try {
			progressStrokeWidth = array.getDimension(R.styleable.CircularChartView_progressStrokeWidth, defaultStrokeWidth)
		} finally {
			array.recycle()
		}
		
		backgroundPaint.strokeWidth = progressStrokeWidth
		progressPaint.strokeWidth = progressStrokeWidth
	}
	
	override fun onDraw(canvas: Canvas) {
		if (segments.isEmpty()) drawCircle(0f, 360f, canvas, backgroundPaint)
		segments.forEach {
			progressPaint.color = it.second
			drawCircle(startAngle, it.first, canvas, progressPaint)
			startAngle += it.first
		}
	}
	
	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		val height = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
		val width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
		val min = min(width, height)
		setMeasuredDimension(min, min)
		val highStroke = progressStrokeWidth
		rect.set(0 + highStroke / 2, 0 + highStroke / 2, min - highStroke / 2, min - highStroke / 2)
	}
	
	override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
		diameter = min(width, height).toFloat()
		updateRect()
	}
	
	private fun updateRect() {
		val strokeWidth = progressStrokeWidth
		rect.set(strokeWidth, strokeWidth, diameter - strokeWidth, diameter - strokeWidth)
	}
	
	private fun drawCircle(startAngle: Float, angle: Float, canvas: Canvas, paint: Paint) {
		canvas.drawArc(rect, startAngle, angle, false, paint)
	}
	
	private fun calculateAngle(progress: Float) = maxAngle / maxProgress * progress
	
	fun setList(list: List<Pair<Float, Int>>) {
		segments = list.map { calculateAngle(it.first) to it.second }
		invalidate()
	}
	
	private val Int.px: Int
		get() = (this * Resources.getSystem().displayMetrics.density).toInt()
}