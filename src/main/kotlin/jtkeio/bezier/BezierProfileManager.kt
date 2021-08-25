package jtkeio.bezier

import kotlin.math.cos
import kotlin.math.sin

class BezierProfileManager(val field: FRCField) {

    fun generateBezierProfile(currentPosition: Pair<Double, Double>, currentHeading: Double, targetPosition: Pair<Double, Double>, targetHeading: Double): BezierProfile {
        val guidePointMultipliers = (targetPosition.first-currentPosition.first) to (targetPosition.second-currentPosition.second)
        val currentGuidePoint = currentPosition.first + cos(currentHeading)*guidePointMultipliers.first to currentPosition.second + sin(currentHeading)*guidePointMultipliers.second
        val targetGuidePoint = targetPosition.first + cos(targetHeading + 180)*guidePointMultipliers.first to targetPosition.second + sin(targetHeading + 180)*guidePointMultipliers.second
        return BezierProfile(arrayOf(currentPosition, currentGuidePoint, targetGuidePoint, targetPosition))
    }
}