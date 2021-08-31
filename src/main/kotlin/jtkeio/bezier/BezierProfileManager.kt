package jtkeio.bezier

import kotlin.math.*


class BezierProfileManager(val field: FRCField) {




    fun generateBezierProfile(currentPosition: Pair<Double, Double>, targetPosition: Pair<Double, Double>, currentHeading: Double = 180*atan2(targetPosition.second-currentPosition.second,targetPosition.first-currentPosition.first)/PI, targetHeading: Double = 180*atan2(targetPosition.second-currentPosition.second, targetPosition.first-currentPosition.first)/PI): BezierProfile {
        if (currentPosition.first<0 || currentPosition.second<0) { throw IllegalArgumentException("currentPosition in function call $this.generateBezierProfile() cannot be a negative number (passed $currentPosition)")
        } else if (targetPosition.first<0 || targetPosition.first<0) { throw IllegalArgumentException("targetPosition in function call $this.generateBezierProfile() cannot be a negative number (passed $targetPosition)")
        } else if (currentPosition.first>8.2 || currentPosition.second>16.6) { throw IllegalArgumentException("currentPosition in function call $this.generateBezierProfile() cannot have coordinates greater than (8.1, 16.5) (passed $currentPosition)")
        } else if (targetPosition.first>8.2 || targetPosition.second>16.6) { throw IllegalArgumentException("targetPosition in function call $this.generateBezierProfile() cannot have coordinates greater than (8.1, 16.5) (passed $targetPosition)") }
        val guidePointMultiplier = sqrt((targetPosition.second-currentPosition.second).pow(2) + (targetPosition.first-currentPosition.first).pow(2)) * 0.6
        val currentGuidePoint = currentPosition.first + cos(PI*currentHeading/180)*guidePointMultiplier to currentPosition.second + sin(PI*currentHeading/180)*guidePointMultiplier
        val targetGuidePoint = targetPosition.first + cos(PI*(180 + targetHeading)/180)*guidePointMultiplier to targetPosition.second + sin(PI*(180 + targetHeading)/180)*guidePointMultiplier
        return BezierProfile(arrayOf(currentPosition, currentGuidePoint, targetGuidePoint, targetPosition)).fitBezierProfileToSelf()
    }

    private fun BezierProfile.fitBezierProfileToSelf(): BezierProfile {
        val bezierProfileTestPoints = Array(256) { this.calculateBezierPoint(it / 256.toDouble()) }
        for (a in bezierProfileTestPoints.indices) {
            for (b in bezierProfileTestPoints.indices) {
                if (a==b) {continue}
                if (sqrt((bezierProfileTestPoints[a].first - bezierProfileTestPoints[b].first).pow(2) + (bezierProfileTestPoints[a].second - bezierProfileTestPoints[b].second).pow(2)) < 0.01) {
                    val idealBezierCurveAngle = (this.derivativeBezierProfile.calculateIntegralBezierAngle(a / 256.toDouble()) + 360) % 180
                    val testBezierCurveAngle = (180 * atan2(y = bezierProfileTestPoints[a].second - bezierProfileTestPoints[b].second, x = bezierProfileTestPoints[a].first - bezierProfileTestPoints[b].first)/PI + 360) % 180
                    if (testBezierCurveAngle + 30 < idealBezierCurveAngle || testBezierCurveAngle - 30 > idealBezierCurveAngle) {
                        val insertIndex = waypoints.size - 2
                        val fittedWaypoints = Array(waypoints.size+1) {
                            if (it < insertIndex) {
                                waypoints[it]
                            } else if (it > insertIndex) {
                                waypoints[it-1]
                            } else {
                                val newWaypointAngle = PI + atan2(abs(waypoints[0].second-bezierProfileTestPoints[a].second), abs(waypoints[0].first-bezierProfileTestPoints[a].first))
                                val newWaypointMultipliers = abs(waypoints[0].first-waypoints[waypoints.lastIndex].first)/2 to abs(waypoints[1].second-waypoints[waypoints.lastIndex].second)/2
                                val x = bezierProfileTestPoints[a].first + cos(newWaypointAngle)*newWaypointMultipliers.second
                                val y = bezierProfileTestPoints[a].second + sin(newWaypointAngle)*newWaypointMultipliers.first
                                x to y
                            }
                        }
                        return BezierProfile(fittedWaypoints)
                    }
                }
            }
        }
        return this
    }

    fun fitBezierProfileToBoundaries(inputBezierProfile: BezierProfile) {

    }

    fun fitBezierProfileToObstacles(inputBezierProfile: BezierProfile) {

    }
}