package jtkeio.bezier

import kotlin.math.*


class BezierProfileManager(val field: FRCField) {


    fun generateBezierProfile(currentPosition: Pair<Double, Double>, targetPosition: Pair<Double, Double>, currentHeading: Double = 180*atan2(targetPosition.second-currentPosition.second,targetPosition.first-currentPosition.first)/PI, targetHeading: Double = 180*atan2(targetPosition.second-currentPosition.second, targetPosition.first-currentPosition.first)/PI): BezierProfile {
        if (currentPosition.first<0 || currentPosition.second<0) { throw IllegalArgumentException("currentPosition in function call $this.generateBezierProfile() cannot be a negative number (passed $currentPosition)") }
        else if (targetPosition.first<0 || targetPosition.first<0) { throw IllegalArgumentException("targetPosition in function call $this.generateBezierProfile() cannot be a negative number (passed $targetPosition)") }
        else if (currentPosition.first>8.2 || currentPosition.second>16.6) { throw IllegalArgumentException("currentPosition in function call $this.generateBezierProfile() cannot have coordinates greater than (8.1, 16.5) (passed $currentPosition)") }
        else if (targetPosition.first>8.2 || targetPosition.second>16.6) { throw IllegalArgumentException("targetPosition in function call $this.generateBezierProfile() cannot have coordinates greater than (8.1, 16.5) (passed $targetPosition)") }
        val guidePointMultiplier = sqrt((targetPosition.second-currentPosition.second).pow(2) + (targetPosition.first-currentPosition.first).pow(2)) * 0.6
        val currentGuidePoint = currentPosition.first + cos(PI*currentHeading/180)*guidePointMultiplier to currentPosition.second + sin(PI*currentHeading/180)*guidePointMultiplier
        val targetGuidePoint = targetPosition.first + cos(PI*(180 + targetHeading)/180)*guidePointMultiplier to targetPosition.second + sin(PI*(180 + targetHeading)/180)*guidePointMultiplier
        return BezierProfile(arrayOf(currentPosition, currentGuidePoint, targetGuidePoint, targetPosition)).fitBezierProfileToSelf(2)
    }

    private fun BezierProfile.fitBezierProfileToSelf(cycles: Int = 1): BezierProfile {
        if (cycles == 0) {return this}
        if (waypoints.size < 4) { throw IllegalArgumentException("function fitBezierProfileToSelf() can only be called on BezierProfile objects with at least 4 waypoints (passed $this with waypoints ${this.waypoints})") }
        val bezierProfileTestPoints = Array(256) { this.calculateBezierPoint(it / 256.0) }
        for (p in bezierProfileTestPoints.dropLast(10).indices) {
            if (abs(derivativeBezierProfile.calculateIntegralBezierAngle(p / 256.0) - derivativeBezierProfile.calculateIntegralBezierAngle( (p + 10) / 256.0)) > 30) {
                val closestWaypoint = waypoints.minByOrNull {sqrt((bezierProfileTestPoints[p+5].first - it.first).pow(2) + (bezierProfileTestPoints[p+5].second - it.second).pow(2)) }
                var closestWaypointIndex = if (waypoints.indexOf(closestWaypoint) == 0) {1} else if (waypoints.indexOf(closestWaypoint) == waypoints.lastIndex) {waypoints.lastIndex-1} else {waypoints.indexOf(closestWaypoint)}
                val peakPoint = bezierProfileTestPoints.minByOrNull{ sqrt((it.first-waypoints[closestWaypointIndex].first).pow(2) + (it.second-waypoints[closestWaypointIndex].second).pow(2)) }!!
                val peakPointIndex = bezierProfileTestPoints.indexOf(peakPoint)
                val reflectionPoint = if (closestWaypointIndex == 1) {waypoints[0]} else if (closestWaypointIndex == waypoints.lastIndex-1) {waypoints[waypoints.lastIndex]} else {peakPoint}
                val reflectionLineSlope = (waypoints[closestWaypointIndex].first - reflectionPoint.first) to (waypoints[closestWaypointIndex].second - reflectionPoint.second)
                val fittedBezierProfile = Array(2) { BezierProfile(Array(waypoints.size) { if (it != closestWaypointIndex) {waypoints[it]} else { waypoints[closestWaypointIndex].first + reflectionLineSlope.first*(it-0.5) to waypoints[closestWaypointIndex].second + reflectionLineSlope.second*(it-0.5) }})}.minByOrNull{abs(it.derivativeBezierProfile.calculateIntegralBezierAngle(p / 256.0) - it.derivativeBezierProfile.calculateIntegralBezierAngle( (p + 10) / 256.0))}!!.fitBezierProfileToSelf(cycles-1)
                //abs(derivativeBezierProfile.calculateIntegralBezierAngle(p / 256.0) - derivativeBezierProfile.calculateIntegralBezierAngle( (p + 10) / 256.0)) > 90
                val waypointMultiplier = sqrt((waypoints[0].second-waypoints[waypoints.lastIndex].second).pow(2) + (waypoints[0].first-waypoints[waypoints.lastIndex].first).pow(2)) * 0.5
                return if (true) {
                    if (peakPointIndex/256.0 > 0.5) {
                        BezierProfile(
                            Array(waypoints.size+1){
                                if (it < closestWaypointIndex-1) {
                                    waypoints[it]
                                } else if (it > closestWaypointIndex-1) {
                                    waypoints[it-1]
                                } else {
                                    waypoints[closestWaypointIndex].first + waypointMultiplier*cos(PI*derivativeBezierProfile.calculateIntegralBezierAngle(peakPointIndex/256.0)/180 + PI) to waypoints[closestWaypointIndex].second + waypointMultiplier*sin(PI*derivativeBezierProfile.calculateIntegralBezierAngle(peakPointIndex/256.0)/180 + PI)
                                }
                            }
                        ).fitBezierProfileToSelf(cycles-1)
                    } else {
                        BezierProfile(
                            Array(waypoints.size+1){
                                if (it < closestWaypointIndex+1) {
                                    waypoints[it]
                                } else if (it > closestWaypointIndex+1) {
                                    waypoints[it-1]
                                } else {
                                    waypoints[closestWaypointIndex].first + waypointMultiplier*cos(PI*derivativeBezierProfile.calculateIntegralBezierAngle(peakPointIndex/256.0)/180) to waypoints[closestWaypointIndex].second + waypointMultiplier*sin(PI*derivativeBezierProfile.calculateIntegralBezierAngle(peakPointIndex/256.0)/180)
                                }
                            }
                        ).fitBezierProfileToSelf(cycles-1)
                    }
                } else {fittedBezierProfile.fitBezierProfileToSelf(cycles-1)}
            }
        }
        return this
    }

    fun fitBezierProfileToBoundaries(inputBezierProfile: BezierProfile) {

    }

    fun fitBezierProfileToObstacles(inputBezierProfile: BezierProfile) {

    }
}