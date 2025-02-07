package zm.experiment.model

import zm.experiment.model.type.PlotType
import kotlin.math.*

class Plot (
    val type: PlotType = PlotType.Cartesian,
    val title: String = "",
    private val axis1: Axis = Axis(),
    private val axis2: Axis = Axis(),

    ) {
    val x: Axis get() = axis1
    val y: Axis get() = axis2

    val rho: Axis get() = axis2
    val theta: Axis get() = axis1


}

fun roundToIdeal(num: Double): Double = roundToIdeal(num.toFloat())
fun roundToIdeal(num: Float): Double {

    val lessThanZero = num < 0

    if (num == 0f) return 0.0

    val n: Int = 2
    val d = ceil(log10(if (lessThanZero) -num else num))
    val power = n - d.toInt()

    val magnitude = 10.0.pow(power)
    var shifted = round(num * magnitude)

    val absolute = abs(shifted)

    shifted =
        if (absolute > 75)
            if (lessThanZero) -100.0 else 100.0
        else if (absolute > 30)
            if (lessThanZero) -50.0 else 50.0
        else if (absolute > 23)
            if (lessThanZero) -25.0 else 25.0
        else if (absolute > 15)
            if (lessThanZero) -20.0 else 20.0
        else
            if (lessThanZero) -10.0 else 10.0

    return shifted/magnitude
}
// TODO: Format text label function
//fun formatTextLabel()


data class Axis(
    var min: Float = 0f,
    var max: Float = 500f,
    var segment: Float = 100f,
    var divisions: Int = -1,
    val ticks: Ticks = Ticks(min.toDouble(), max.toDouble(), 5),
    val step : Step = Step(),
    var label: String = "",
    var isLogarithmic: Boolean = false,
    var userDefinedBounds: Boolean = false,
    var autoScale: Boolean = true,
)

data class Divisions(
    var divisions: Int = -1,
    var segment: Float,
)

data class Step(
    var minorStep: Float = -1f,
    var majorStep: Float = -1f,
//    var minorSegment: Float = -1f,
//    var majorSegment: Float = -1f,
)