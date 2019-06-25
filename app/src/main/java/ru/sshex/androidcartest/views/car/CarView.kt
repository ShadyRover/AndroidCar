package ru.sshex.androidcartest.views.car

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import ru.sshex.androidcartest.R
import kotlin.math.hypot

class CarView @JvmOverloads constructor(
	context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
	init {
		initialize(attrs, defStyleAttr)
	}

	private var movingFlag = false
	private var initFrame = false

	private var carFrame: CarFrame =
		CarFrame(speed = 3000, angle = 90.0)
	private var animator: ValueAnimator? = null

	private var interpolator: Interpolator = AccelerateDecelerateInterpolator()
	private lateinit var carBitmap: Bitmap
	private lateinit var carMatrix: Matrix


	private fun initialize(attrs: AttributeSet?, defStyleAttr: Int) {
		// Load attributes
		val a = context.obtainStyledAttributes(
			attrs, R.styleable.CarView, defStyleAttr, 0
		)
		val drawable = a.getDrawable(R.styleable.CarView_carDrawable) ?: resources.getDrawable(R.drawable.ic_r_car, null)
		a.recycle()

		val bm: Bitmap
		if (drawable is BitmapDrawable) {
			bm = drawable.bitmap
		} else {
			bm = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
			val canvas = Canvas(bm)
			drawable.setBounds(0, 0, canvas.width, canvas.height)
			drawable.draw(canvas)
		}
		carBitmap = Bitmap.createScaledBitmap(bm, bm.width / 2, bm.height / 2, false)
		carMatrix = Matrix()
	}

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		if (!initFrame) {
			val width = MeasureSpec.getSize(widthMeasureSpec)
			val height = MeasureSpec.getSize(heightMeasureSpec)
			carFrame.x = width / 2f
			carFrame.y = height - (height / 5f)
			carFrame.saveLastXY()
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec)
	}

	override fun onDraw(canvas: Canvas) {
		carMatrix.reset()
		carMatrix.setRotate((90 - carFrame.angle).toFloat(), carBitmap.width / 2f, carBitmap.height / 2f)
		carMatrix.postTranslate(carFrame.x - carBitmap.width / 2f, carFrame.y - carBitmap.height / 2f)
		canvas.drawBitmap(carBitmap, carMatrix, null)
	}

	@SuppressLint("ClickableViewAccessibility")
	override fun onTouchEvent(event: MotionEvent): Boolean {
		return if (!movingFlag) {
			moveToPoint(event.x, event.y)
			false
		} else {
			true
		}
	}

	private fun moveToPoint(toX: Float, toY: Float) {
		movingFlag = true

		val bezierPoint = carFrame.getBezierPoint(width.toFloat(), height.toFloat())
		animator = ValueAnimator.ofFloat(0f, 1f)
		bezierPoint?.run {
			val bx = first
			val by = second

			animator?.apply {
				duration = carFrame.speed
				interpolator = this@CarView.interpolator
				addUpdateListener {
					carFrame.x = BezierUtils
						.calcBezier(it.animatedValue as Float, carFrame.tempX, bx, toX)
					carFrame.y = BezierUtils
						.calcBezier(it.animatedValue as Float, carFrame.tempY, by, toY)
					carFrame.angle = BezierUtils.calcAngle(
						it.animatedValue as Float,
						carFrame.tempX,
						-carFrame.tempY,
						bx,
						-by,
						toX,
						-toY
					)
					Log.d("CarView", "moveToPoint() carAngle = [${carFrame.angle}]")
					postInvalidateOnAnimation()
				}
				addListener(
					object : AnimatorListenerAdapter() {
						override fun onAnimationEnd(animation: Animator) {
							movingFlag = false
							carFrame.saveLastXY()
						}
					}
				)
				start()
			}
		}
	}

	fun setSpeed(speed: Long) {
		carFrame.speed = speed
	}

	fun stopAnimation() {
		animator?.cancel()
	}

	data class CarFrame(
		var x: Float = 0f,
		var y: Float = 0f,
		var speed: Long = 3000,
		var tempX: Float = 0f,
		var tempY: Float = 0f,
		var angle: Double
	) {
		companion object {
			val borderK = Math.tan(Math.toRadians(90.0))
		}

		fun saveLastXY() {
			tempX = x
			tempY = y
		}

		fun getBezierPoint(rootWidth: Float, rootHeight: Float): Pair<Float, Float>? {
			//y = kx - b
			val k = Math.tan(Math.toRadians(angle))
			val b = -y - k * x

			when (angle.toInt()) {
				in 0..90 -> {
					val cX = -b / k
					val borderB = -y - borderK * rootWidth
					return calcXYForTopSector(cX.toFloat(), b = b, k = k, borderK = borderK, borderB = borderB)
				}
				in 91..180 -> {
					val cX = -b / k
					val borderB = -y.toDouble()
					return calcXYForTopSector(cX.toFloat(), b = b, k = k, borderK = borderK, borderB = borderB)
				}

				in 181..270 -> {
					//check left and bot border
					val cX = (-rootHeight - b) / k

					val borderB = -y.toDouble()

					return calcXYForTopSector(cX.toFloat(), rootHeight, b = b, k = k, borderK = borderK, borderB = borderB)
				}
				in 271..360 -> {
					//check bot and right border
					val cX = (-rootHeight - b) / k

					val borderB = -y - borderK * rootWidth.toDouble()
					return calcXYForTopSector(cX.toFloat(), rootHeight, b = b, k = k, borderK = borderK, borderB = borderB)

				}
			}
			return null
		}

		private fun calcXYForTopSector(
			xPoint: Float,
			yPoint: Float = 0f,
			b: Double,
			k: Double,
			borderK: Double,
			borderB: Double
		): Pair<Float, Float> {
			val cXRight = ((borderB - b) / (k - borderK))
			val cYRight = -(k * cXRight + b)
			val distanceEnd = hypot((x - xPoint).toDouble(), (y - yPoint).toDouble())
			val distanceStart = hypot(x - cXRight, y - cYRight)

			return if (distanceStart < distanceEnd) {
				Pair(cXRight.toFloat(), cYRight.toFloat())
			} else {
				Pair(xPoint, yPoint)
			}
		}
	}
}
