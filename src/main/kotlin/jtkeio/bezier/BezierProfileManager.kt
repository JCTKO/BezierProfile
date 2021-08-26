package jtkeio.bezier

import java.io.File
import javax.imageio.ImageIO
import kotlin.math.*

class BezierProfileManager(val field: FRCField) {


    fun generateBezierProfile(currentPosition: Pair<Double, Double>, currentHeading: Double, targetPosition: Pair<Double, Double>, targetHeading: Double): BezierProfile {
        val guidePointMultipliers = (targetPosition.first-currentPosition.first) to (targetPosition.second-currentPosition.second)
        val currentGuidePoint = currentPosition.first + cos(PI*currentHeading/180)*guidePointMultipliers.first to currentPosition.second + sin(PI*currentHeading/180)*guidePointMultipliers.second
        val targetGuidePoint = targetPosition.first + cos(PI*(targetHeading - 180)/180)*guidePointMultipliers.first to targetPosition.second + sin(PI*(targetHeading - 180)/180)*guidePointMultipliers.second
        return BezierProfile(arrayOf(currentPosition, currentGuidePoint, targetGuidePoint, targetPosition))
    }
}