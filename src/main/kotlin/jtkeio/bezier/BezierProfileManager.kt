package jtkeio.bezier

import kotlin.math.*


class BezierProfileManager(val field: FRCField) {


    fun generateBezierProfile(currentPosition: Pair<Double, Double>, targetPosition: Pair<Double, Double>, currentHeading: Double = 180*atan((targetPosition.second-currentPosition.second) / (targetPosition.first-currentPosition.first))/PI, targetHeading: Double = 180*atan((targetPosition.second-currentPosition.second) / (targetPosition.first-currentPosition.first))/PI): BezierProfile {
        val guidePointMultiplier = sqrt((targetPosition.second-currentPosition.second).pow(2) + (targetPosition.first-currentPosition.first).pow(2)) / 2
        val currentGuidePoint = currentPosition.first + cos(PI*currentHeading/180)*guidePointMultiplier to currentPosition.second + sin(PI*currentHeading/180)*guidePointMultiplier
        val targetGuidePoint = targetPosition.first + cos(PI*(targetHeading - 180)/180)*guidePointMultiplier to targetPosition.second + sin(PI*(targetHeading - 180)/180)*guidePointMultiplier
        return BezierProfile(arrayOf(currentPosition, currentGuidePoint, targetGuidePoint, targetPosition))
    }
}