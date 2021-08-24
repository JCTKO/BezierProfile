package jtkeio.bezier


class FRCField(vararg initialBoundaries: Boundary) {

    val fieldBoundaries = arrayListOf<Boundary>().apply{
        addAll(initialBoundaries)
    }
}