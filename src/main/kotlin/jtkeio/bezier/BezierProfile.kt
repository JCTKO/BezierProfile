package jtkeio.bezier

import java.awt.Color
import java.awt.image.BufferedImage
import kotlin.collections.ArrayList
import kotlin.math.*


//2D Bézier curves for FRC pathfinding
class BezierProfile(val waypoints: Array<Pair<Double, Double>>) {

    //This profile is used to calculate the angle of a curve by calling calculateIntegralBezierAngle() on the derivativeBezierProfile of the original curve
    val derivativeBezierProfile: BezierProfile by lazy {BezierProfile(Array(waypoints.size - 1){ i -> (waypoints.size) * (waypoints[i + 1].first - waypoints[i].first) to (waypoints.size) * (waypoints[i + 1].second - waypoints[i].second)})}
    private companion object {@JvmStatic private val binomialCoefficients = ArrayList<Array<Int>>(8).apply{add(arrayOf(1))}}


    //Utility function that recursively generates binomial coefficients by pascal's triangle
    fun calculateBinomialCoefficients(numberOfCoefficients: Int = waypoints.size): Array<Int> {
        if (numberOfCoefficients < 1) throw IllegalArgumentException("numberOfCoefficients must be greater than 0 to calculate binomial coefficients (passed $numberOfCoefficients)")
        return if (numberOfCoefficients <= binomialCoefficients.size) {binomialCoefficients[numberOfCoefficients-1]} else {
            val old = calculateBinomialCoefficients(numberOfCoefficients-1).copyInto(Array(numberOfCoefficients+1){0}, 1)
            val new = Array(numberOfCoefficients){old[it] + old[it+1]}
            binomialCoefficients.add(new); new
        }
    }

    //Returns a pair of x/y coordinates on the curve given a number from 0 to 1 representing the distance along the curve from the first waypoint to the last //the semicolons stay >:)
    fun calculateBezierPoint(howFarAlongFromZeroToOne: Double): Pair<Double, Double> {
        if (howFarAlongFromZeroToOne<0 || howFarAlongFromZeroToOne>1) throw IllegalArgumentException("howFarAlongFromZeroToOne must be between or equal to 0 and 1 (passed $howFarAlongFromZeroToOne)")
        var x = 0.0; var y = 0.0; val binomialCoefficients = calculateBinomialCoefficients()
        for (n in waypoints.indices) {
            x += waypoints[n].first * binomialCoefficients[n] * (1 - howFarAlongFromZeroToOne).pow(waypoints.size - 1 - n) * (howFarAlongFromZeroToOne).pow(n)
            y += waypoints[n].second * binomialCoefficients[n] * (1 - howFarAlongFromZeroToOne).pow(waypoints.size - 1 - n) * (howFarAlongFromZeroToOne).pow(n)
        }; return Pair(x, y)
    }

    //Returns the angle of the line tangent to a curve at a point between the first and last waypoint (0 to 1) when called on its derivativeBezierProfile
    fun calculateIntegralBezierAngle(howFarAlongFromZeroToOne: Double): Double {
        if (howFarAlongFromZeroToOne<0 || howFarAlongFromZeroToOne>1) throw IllegalArgumentException("howFarAlongFromZeroToOne must be between or equal to 0 and 1 (passed $howFarAlongFromZeroToOne)")
        val point = calculateBezierPoint(howFarAlongFromZeroToOne)
        return atan2(y = point.second, x = point.first)*180/PI
    }

    //Returns a 256x512 jpg of this Bézier curve drawn in green or a given color (over another image if given), representing a path over the FRC field
    fun drawBezierProfile(imageOnWhichToDraw: BufferedImage = BufferedImage(256, 512, BufferedImage.TYPE_INT_RGB), color: Color = Color(0, 200, 0), resolution: Int = 1024): BufferedImage {
        if (imageOnWhichToDraw.width != 256 || imageOnWhichToDraw.height != 512) {
            throw IllegalArgumentException("Image passed into drawBezierProfile() in BezierProfile $this must have dimensions 256x512 (passed ${imageOnWhichToDraw.width}x${imageOnWhichToDraw.height})")
        } else {
            val workingImage = BufferedImage(256, 512, BufferedImage.TYPE_INT_RGB)
            workingImage.data = imageOnWhichToDraw.data
            return workingImage.apply {
                val graphicBezier = BezierProfile(waypoints.map { it.first * 25 to it.second * 25 }.toTypedArray())
                for (z in 0 until resolution) {
                    val (x, y) = graphicBezier.calculateBezierPoint(z / resolution.toDouble())
                    if (x < 0 || x > 230 || y < 0 || y > 412) { continue }
                    setRGB(x.roundToInt() + 25, this.height - y.roundToInt() - 45, color.rgb)
                }
                for (w in waypoints) {
                    val x = w.first * 25; val y = w.second * 25
                    if (x < 0 || x > 230 || y < 0 || y > 412) { continue }
                    setRGB(x.roundToInt() + 25, this.height - y.roundToInt() - 45, Color(50, 200, 255).rgb)
                }
            }
        }
    }
}