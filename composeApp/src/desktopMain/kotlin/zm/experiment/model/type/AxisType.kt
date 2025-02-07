package zm.experiment.model.type

enum class AxisType {
    X, Y,
}

fun parseAxis(axis: String): AxisType? {
    return when (axis.lowercase()) {
        "x" -> AxisType.X
        "y" -> AxisType.Y
        else -> null
    }
}