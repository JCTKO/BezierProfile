package jtkeio.bezier


class FRCField(vararg initialBoundaries: Pair<Double, Double>) {

    val fieldBoundaries = arrayListOf<Pair<Double, Double>>().addAll(initialBoundaries)
}