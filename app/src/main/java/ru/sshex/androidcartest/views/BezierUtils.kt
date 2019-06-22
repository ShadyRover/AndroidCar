package ru.sshex.androidcartest.views

import kotlin.math.atan2

class BezierUtils {
	companion object {
		fun calcBezier(time: Float, p0: Float, p1: Float, p2: Float): Float {
			return (Math.pow((1 - time).toDouble(), 2.0) * p0
					+ (2f * (1 - time) * time * p1).toDouble()
					+ Math.pow(time.toDouble(), 2.0) * p2).toFloat()

		}

		/**
		 * move of car - simple quadratic bezier function with 3 point
		 * https://en.wikipedia.org/wiki/B%C3%A9zier_curve#Quadratic_B%C3%A9zier_curves
		 * @param   time   - the time from start.z
		 * @param   p0x     - the start point x coord
		 * @param   p0y     - the start point y coord
		 * @param   p1x     - the control point x coord
		 * @param   p1y     - the control point y coord
		 * @param   p2x     - the end point x coord
		 * @param   p2y     - the end point y coord
		 * @return  the value of current angle of the car with entered time value
		 */
		fun calcAngle(time: Float, p0x: Float, p0y: Float, p1x: Float, p1y: Float, p2x: Float, p2y: Float): Double {
			val t = 1.0f - time
			val dx = ((t * p1x + time * p2x) - (t * p0x + time * p1x)).toDouble()
			val dy = ((t * p1y + time * p2y) - (t * p0y + time * p1y)).toDouble()
			var theta = Math.toDegrees(atan2(dy, dx))
			if (theta < 0.0f)
				theta += 360.0
			return theta
		}
	}
}