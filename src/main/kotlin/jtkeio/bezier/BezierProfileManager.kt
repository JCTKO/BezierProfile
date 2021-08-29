package jtkeio.bezier

import kotlin.math.*


class BezierProfileManager(val field: FRCField) {

    val obstacleAvoidanceWaypoints = ArrayList<Int>()


    fun generateBezierProfile(currentPosition: Pair<Double, Double>, targetPosition: Pair<Double, Double>, currentHeading: Double = 180*atan((targetPosition.second-currentPosition.second) / (targetPosition.first-currentPosition.first))/PI, targetHeading: Double = 180*atan((targetPosition.second-currentPosition.second) / (targetPosition.first-currentPosition.first))/PI): BezierProfile {
        if (currentPosition.first<0 || currentPosition.second<0) {
            throw IllegalArgumentException("currentPosition in function call $this.generateBezierProfile() cannot be a negative number (passed $currentPosition)")
        } else if (targetPosition.first<0 || targetPosition.first<0) {
            throw IllegalArgumentException("targetPosition in function call $this.generateBezierProfile() cannot be a negative number (passed $targetPosition)")
        } else if (currentPosition.first>8.2 || currentPosition.second>16.6) {
            throw IllegalArgumentException("currentPosition in function call $this.generateBezierProfile() cannot have coordinates greater than (8.1, 16.5) (passed $currentPosition)")
        } else if (targetPosition.first>8.2 || targetPosition.second>16.6) {
            throw IllegalArgumentException("targetPosition in function call $this.generateBezierProfile() cannot have coordinates greater than (8.1, 16.5) (passed $targetPosition)")
        }
        val guidePointMultiplier = sqrt((targetPosition.second-currentPosition.second).pow(2) + (targetPosition.first-currentPosition.first).pow(2)) * 0.6
        val currentGuidePoint = currentPosition.first + cos(PI*currentHeading/180)*guidePointMultiplier to currentPosition.second + sin(PI*currentHeading/180)*guidePointMultiplier
        val targetGuidePoint = targetPosition.first + cos(PI*(targetHeading - 180)/180)*guidePointMultiplier to targetPosition.second + sin(PI*(targetHeading - 180)/180)*guidePointMultiplier
        return BezierProfile(arrayOf(currentPosition, currentGuidePoint, targetGuidePoint, targetPosition))
    }
}