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
    field.addObstacle((4.0 to 4.0) to (4.0 to 5.0))
    val fieldImage = field.drawField()
    val manager = BezierProfileManager(field)

    val label = JLabel().apply {
        setSize(256, 512)
        isVisible = true
    }
    JFrame("BezierProfileManagerTest").apply {
        setSize(272, 528)
        add(label)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        isVisible = true
    }

    while (true) {
        for (i in 0 until 2513) {
            val baseProfile = manager.generateBezierProfile(4.0 to 5.0, 5.0 to 4.0, i%360.toDouble(), -i/7.0)
            val currentImage = baseProfile.drawBezierProfile(fieldImage)
            label.icon = ImageIcon(currentImage)
            Thread.sleep(30)
        }
    }
}