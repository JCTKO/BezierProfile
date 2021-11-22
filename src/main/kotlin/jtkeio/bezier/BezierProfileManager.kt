package jtkeio.bezier

import kotlin.math.*


class BezierProfileManager(val field: FRCField) {

    fun generateBezierProfile(currentPosition: Pair<Double, Double>, targetPosition: Pair<Double, Double>): BezierProfile {
        if (currentPosition.first<0 || currentPosition.second<0) throw IllegalArgumentException("currentPosition in function call $this.generateBezierProfile() cannot be a negative number (passed $currentPosition)")
        else if (targetPosition.first<0 || targetPosition.first<0) throw IllegalArgumentException("targetPosition in function call $this.generateBezierProfile() cannot be a negative number (passed $targetPosition)")
        else if (currentPosition.first>8.2 || currentPosition.second>16.6) throw IllegalArgumentException("currentPosition in function call $this.generateBezierProfile() cannot have coordinates greater than (8.1, 16.5) (passed $currentPosition)")
        else if (targetPosition.first>8.2 || targetPosition.second>16.6) throw IllegalArgumentException("targetPosition in function call $this.generateBezierProfile() cannot have coordinates greater than (8.1, 16.5) (passed $targetPosition)")
        return BezierProfile(arrayOf(currentPosition, targetPosition))
    }

    private fun BezierProfile.fitBezierProfileToObstacles(): BezierProfile {
        return this
    }

    private fun BezierProfile.routeBezierProfileThroughGuidePoints(): BezierProfile {
        return this
    }
}