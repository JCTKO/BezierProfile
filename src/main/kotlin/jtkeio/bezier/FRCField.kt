package jtkeio.bezier

import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt


class FRCField(vararg initialBoundaries: Pair<Pair<Double, Double>, Pair<Double, Double>>) {

    val fieldBoundaries: ArrayList<Pair<Pair<Double, Double>, Pair<Double, Double>>> = arrayListOf<Pair<Pair<Double, Double>, Pair<Double, Double>>>().apply { addAll(initialBoundaries) }
    val fieldObstacles: ArrayList<Pair<Pair<Double, Double>, Pair<Double, Double>>> = arrayListOf()

    fun drawField(imageOnWhichToDraw: BufferedImage = BufferedImage(256, 256, TYPE_INT_RGB)): BufferedImage {
        return imageOnWhichToDraw.apply {
            fieldBoundaries.forEach { boundary ->
                val boundaryBezier = BezierProfile(arrayOf(boundary.first.run{this.first*16 to this.second*16}, boundary.second.run{this.first*16 to this.second*16}))
                val boundaryPixels = (sqrt((boundary.first.second - boundary.second.second).pow(2) + (boundary.first.first - boundary.second.first).pow(2))*16).roundToInt()
                for (z in 0 until boundaryPixels) {
                    val (x, y) = boundaryBezier.calculateBezierPoint(z / boundaryPixels.toDouble())
                    setRGB(x.roundToInt(), this.height-y.roundToInt()-1, Color(200, 200, 200).rgb)
                }
            }
            fieldObstacles.forEach { obstacle ->
                val obstacleBezier = BezierProfile(arrayOf(obstacle.first.run{this.first*16 to this.second*16}, obstacle.second.run{this.first*16 to this.second*16}))
                val obstaclePixels = (sqrt((obstacle.first.second - obstacle.second.second).pow(2) + (obstacle.first.first - obstacle.second.first).pow(2))*16).roundToInt()
                for (z in 0 until obstaclePixels) {
                    val (x, y) = obstacleBezier.calculateBezierPoint(z / obstaclePixels.toDouble())
                    setRGB(x.roundToInt(), this.height-y.roundToInt(), Color(180, 0, 0).rgb)
                }
            }
        }
    }
}

fun main() {
    val f = FRCField().apply {
        fieldBoundaries.apply {
            add((0.0 to 0.0) to (10.0 to 0.0))
            add((10.0 to 0.0) to (10.0 to 10.0))
            add((10.0 to 0.0) to (0.0 to 10.0))
            add((0.0 to 10.0) to (10.0 to 10.0))
        }
        fieldObstacles.apply {
            add((5.0 to 5.0) to (10.0 to 7.0))
        }
    }.drawField()
    ImageIO.write(f, "jpg", File("C:/Users/Jacob Tkeio/Desktop/frcfield.jpg"))
}