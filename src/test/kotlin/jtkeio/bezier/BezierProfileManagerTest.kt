package jtkeio.bezier

import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.JLabel


fun main() {
    val field = FRCField(
        (0.0 to 0.0) to (8.1 to 0.0),
        (8.1 to 0.0) to (8.1 to 16.5),
        (8.1 to 16.5) to (0.0 to 16.5),
        (0.0 to 16.5) to (0.0 to 0.0)
    )
    field.addObstacle((2.0 to 2.0) to (4.0 to 2.5))
    val fieldImage = field.drawField()
    val manager = BezierProfileManager(field)

    val frame = JFrame("BezierProfileManagerTest")
    val label = JLabel()
    label.setSize(256, 512)
    label.isVisible = true
    frame.setSize(272, 528)
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.add(label)
    frame.isVisible = true

    while (true) {
        for (i in 0 until 90) {
            val currentImage = manager.generateBezierProfile(4.0 to 6.0, 5.0 to 12.0, 4*i.toDouble(), 0.0).drawBezierProfile(fieldImage)
            label.icon = ImageIcon(currentImage)
            Thread.sleep(100)
        }
    }
}