package jtkeio.bezier

import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt


//Line-based field descriptions for FRC pathfinding
class FRCField(vararg initialBoundaries: Pair<Pair<Double, Double>, Pair<Double, Double>>) {

    //Boundaries are permanent (like the field walls) and obstacles are temporary (like other robots)
    private val fieldBoundaries: ArrayList<Pair<Pair<Double, Double>, Pair<Double, Double>>> = arrayListOf<Pair<Pair<Double, Double>, Pair<Double, Double>>>().apply { addAll(initialBoundaries) }
    private val fieldObstacles: ArrayList<Pair<Pair<Double, Double>, Pair<Double, Double>>> = arrayListOf()


    //Boundaries are described as a line segment between two points and remain permanently on the FRCField
    fun addBoundary(boundary: Pair<Pair<Double, Double>, Pair<Double, Double>>) {
        if (boundary.first.first<0 || boundary.first.second<0 || boundary.second.first<0 || boundary.second.second<0) {
            throw IllegalArgumentException("Boundary-defining points must have coordinates greater than 0 (passed $boundary)")
        } else if (boundary.first.first>8.2 || boundary.first.second>16.6 || boundary.second.first>8.2 || boundary.second.second>16.6) {
            throw IllegalArgumentException("Boundary-defining points must have coordinates within (8.1, 16.5) (passed $boundary)")
        } else {
            fieldBoundaries.add(boundary)
        }
    }

    //Obstacles are identical to boundaries but can be cleared/updated throughout the match
    fun addObstacle(obstacle: Pair<Pair<Double, Double>, Pair<Double, Double>>) {
        if (obstacle.first.first<0 || obstacle.first.second<0 || obstacle.second.first<0 || obstacle.second.second<0) {
            throw IllegalArgumentException("Obstacle-defining points must have coordinates greater than 0 (passed $obstacle)")
        } else if (obstacle.first.first>8.2 || obstacle.first.second>16.6 || obstacle.second.first>8.2 || obstacle.second.second>16.6) {
            throw IllegalArgumentException("Obstacle-defining points must have coordinates within (8.1, 16.5) (passed $obstacle)")
        } else {
            fieldObstacles.add(obstacle)
        }
    }

    //Clears all obstacles
    fun clearObstacles() = fieldObstacles.clear()

    //Returns a 256x512 jpg of the field with boundaries drawn in white and obstacles drawn in red
    fun drawField(imageOnWhichToDraw: BufferedImage = BufferedImage(256, 512, TYPE_INT_RGB)): BufferedImage {
        if (imageOnWhichToDraw.width!=256 || imageOnWhichToDraw.height != 512) {
            throw IllegalArgumentException("Image passed into drawField() in FRCField $this must have dimensions 256x512 (passed ${imageOnWhichToDraw.width}x${imageOnWhichToDraw.height})")
        } else {
            return imageOnWhichToDraw.apply {
                fieldBoundaries.forEach { boundary ->
                    val boundaryBezier = BezierProfile(arrayOf(boundary.first.run { this.first * 25 to this.second * 25 }, boundary.second.run { this.first * 25 to this.second * 25 }))
                    val boundaryPixels = (sqrt((boundary.first.second - boundary.second.second).pow(2) + (boundary.first.first - boundary.second.first).pow(2)) * 25).roundToInt()
                    for (z in 0 until boundaryPixels) {
                        val (x, y) = boundaryBezier.calculateBezierPoint(z / boundaryPixels.toDouble())
                        setRGB(x.roundToInt() + 25, this.height - y.roundToInt() - 45, Color(200, 200, 200).rgb)
                    }
                }
                fieldObstacles.forEach { obstacle ->
                    val obstacleBezier = BezierProfile(arrayOf(obstacle.first.run { this.first * 25 to this.second * 25 }, obstacle.second.run { this.first * 25 to this.second * 25 }))
                    val obstaclePixels = (sqrt((obstacle.first.second - obstacle.second.second).pow(2) + (obstacle.first.first - obstacle.second.first).pow(2)) * 25).roundToInt()
                    for (z in 0 until obstaclePixels) {
                        val (x, y) = obstacleBezier.calculateBezierPoint(z / obstaclePixels.toDouble())
                        setRGB(x.roundToInt() + 25, this.height - y.roundToInt() - 44, Color(180, 0, 0).rgb)
                    }
                }
            }
        }
    }
}