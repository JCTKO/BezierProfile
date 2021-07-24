package jtkeio.bezier

import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.pow


//FRC-targeted 2D bezier curve manager for smooth, accurate movement //entirely unoptimized lol
class BezierProfile(val waypoints: Array<Pair<Double,Double>>) {

    //This profile is used to calculate the angle of a curve by calling calculateIntegralBezierAngle() on the derivativeBezierProfile of the original curve
    val derivativeBezierProfile: BezierProfile by lazy {BezierProfile(Array(waypoints.size - 1){ i -> (waypoints.size) * (waypoints[i + 1].first - waypoints[i].first) to (waypoints.size) * (waypoints[i + 1].second - waypoints[i].second)})}


    //Utility function that recursively generates binomial coefficients by pascal's triangle
    private fun calculateBinomialCoefficients(numberOfCoefficients: Int = waypoints.size): Array<Int> {
        if (numberOfCoefficients < 1) {throw IllegalArgumentException("numberOfCoefficients must be greater than 0 to calculate binomial coefficients (passed $numberOfCoefficients)")}
        if (numberOfCoefficients==1) {return arrayOf(1)} else {
            val old = calculateBinomialCoefficients(numberOfCoefficients-1).copyInto(Array(numberOfCoefficients+1){0}, 1)
            return Array(numberOfCoefficients){old[it] + old[it+1]}
        }
    }

    //Returns a pair of x/y coordinates on the curve given a number from 0 to 1 representing the distance along the curve from the first waypoint to the last //the semicolons stay >:)
    fun calculateBezierPoint(howFarAlongFromZeroToOne: Double): Pair<Double, Double> {
        if (howFarAlongFromZeroToOne<0 || howFarAlongFromZeroToOne>1) {throw IllegalArgumentException("howFarAlongFromZeroToOne must be between or equal to 0 and 1 (passed $howFarAlongFromZeroToOne)")}
        var x = 0.0; var y = 0.0; val binomialCoefficients = calculateBinomialCoefficients()
        for (n in waypoints.indices) {
            x += waypoints[n].first * binomialCoefficients[n] * (1 - howFarAlongFromZeroToOne).pow(waypoints.size - 1 - n) * (howFarAlongFromZeroToOne).pow(n)
            y += waypoints[n].second * binomialCoefficients[n] * (1 - howFarAlongFromZeroToOne).pow(waypoints.size - 1 - n) * (howFarAlongFromZeroToOne).pow(n)
        }; return Pair(x, y)
    }

    //Returns the angle of the line tangent to a curve at a point between the first and last waypoint (0 to 1) when called on its derivativeBezierProfile
    fun calculateIntegralBezierAngle(howFarAlongFromZeroToOne: Double): Double {
        if (howFarAlongFromZeroToOne<0 || howFarAlongFromZeroToOne>1) {throw IllegalArgumentException("howFarAlongFromZeroToOne must be between or equal to 0 and 1 (passed $howFarAlongFromZeroToOne)")}
        val point = calculateBezierPoint(howFarAlongFromZeroToOne)
        return atan2(y = point.second, x = point.first)*180/PI
    }
}