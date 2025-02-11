package zm.experiment.model

import zm.experiment.model.type.PlotType
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.*



class Plot (
    var id: Int,
    var type: PlotType = PlotType.Cartesian,
    var title: String = "",
    val traces: MutableList<Int> = mutableListOf(),
    private val axis1: Axis = Axis(),
    private val axis2: Axis = Axis(),
    ) {
    private val axes = listOf(axis1, axis2)
    fun reset() {
        type = PlotType.Cartesian
        title = ""
        traces.clear()
        traces.add(id)
        axes.forEach { it.reset() }
    }

    val x: Axis get() = axis1
    val y: Axis get() = axis2

    val rho: Axis get() = axis2
    val theta: Axis get() = axis1


}




data class Axis(
    var min: Float = 0f,
    var max: Float = 500f,
    var segment: Float = 100f,
    var divisions: Int = -1,
    val step : Step = Step(),
    var label: String = "",
    var isLogarithmic: Boolean = false,
    var userDefinedBounds: Boolean = false,
    var autoScale: Boolean = true,
) {
    fun reset() {
        divisions = -1
        label = ""
        isLogarithmic = false
        userDefinedBounds = false
        autoScale = true
        min = 0f
        max = 500f
        segment = 100f
    }

}

fun Axis.range() = sequence {
    var current = min
    while (current <= max) {
        yield(current)
        current += segment
    }
}

fun Axis.calculate(min: Double, max: Double, tickCount: Int = 5) = calculate(min.toFloat(), max.toFloat(), tickCount)
fun Axis.calculate(min: Float, max: Float, tickCount: Int = 5) {
    divisions = tickCount
    segment = (max - min) / divisions
    val range = max - min
    val exp = if (range == 0f) {
        0f
    } else {
        floor(log10(range / (divisions - 1)))
    }
    val scale: Float = 10f.pow(exp)

    val rawTickStep = (range / (divisions - 1)) / scale
    for (potentialStep in floatArrayOf(1f, 1.5f, 2.0f, 2.5f, 3.0f, 4.0f, 5.0f, 6.0f, 8.0f, 10.0f)) {
        if (potentialStep < rawTickStep) {
            continue
        }

        segment = potentialStep * scale
        this.min = segment * floor(min / segment)
        this.max = this.min + segment * (divisions - 1)
        if (this.max >= max) {
            break
        }
    }

    divisions -= floor((this.max - max) / segment).toInt()
}


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

// roundToIdeal((max - min) * (textHeight * 2) / (b - t)
//fun Double.roundToIdeal() = roundToIdeal(this)
//fun Float.roundToIdeal() = roundToIdeal(this)
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

fun Double.formatLabelText(precision: Int): String {
    return _formatLabelText(this, precision)
}

fun Float.formatLabelText(precision: Int): String {
    return _formatLabelText(this.toDouble(), precision)
}

fun Int.formatLabelText(precision: Int): String {
    return _formatLabelText(this.toDouble(), precision)
}

private fun _formatLabelText(labelNumber: Double, precision: Int): String {
    // Scientific notation
    val labelScientific = labelNumber.toStringScientific(precision)

    // Decimal notation
    val labelDecimal = labelNumber.toStringDecimal(precision)

    return when {
        labelDecimal == "-0" -> "0"
        labelDecimal.length < 8 || (labelDecimal.startsWith("-") && labelDecimal.length < 9) -> labelDecimal
        else -> labelScientific
    }
}

// Extension to format in scientific notation
private fun Double.toStringScientific(precision: Int): String {
    var formatted = "%.${precision}E".format(this)
        .replace("+", "")                             // Remove "+" in exponent
        .replace(Regex("(\\.\\d*?)0*E"), "$1E")       // Remove trailing zeros after decimal before "E"
        .replace(Regex("E0*(\\d)"), "E$1")            // Remove leading zeros in exponent
        .replace(Regex("\\.$"), "")                   // Remove dangling decimal point

    // Special case: if ".E", make it "0E"
    if (formatted.contains(".E")) {
        formatted = formatted.replace(Regex("(\\d\\.*?)E"), "$10E")
    }
    return formatted
}

// Extension to format in decimal notation
private fun Double.toStringDecimal(initialPrecision: Int): String {
    var precision = initialPrecision
    var formatted = "%.${precision}f".format(this).removeTrailingZeros()

    if (this in 0.0..1.0 || this in -1.0..0.0) {
        val bigDecimal = BigDecimal(this.toString())
        val thresholds = listOf(0.1, 0.01, 0.001, 0.0001, 0.00001, 0.000001)

        for ((i, threshold) in thresholds.withIndex()) {
            if (abs(this) < threshold) {
                precision = i + 2
                break
            }
        }
        formatted = bigDecimal.setScale(precision, RoundingMode.HALF_DOWN).toPlainString()
    }

    // Special case for 0.10
    if (this == 0.10) return "0.10"

    return formatted
}

// Extension to remove trailing zeros and unnecessary decimal points
private fun String.removeTrailingZeros(): String {
    return this.replace(Regex("0+$"), "").replace(Regex("\\.$"), "")
}



fun calculateRequiredPrecision(maxValue: Float, minValue: Float, segments: Float): Int =
    calculateRequiredPrecision(maxValue.toDouble(), minValue.toDouble(), segments.toDouble())

fun calculateRequiredPrecision(maxValue: Double, minValue: Double, segments: Double): Int {
    if (segments == 0.0 || maxValue == minValue) return 1

    // Determine the largest absolute value between maxValue and minValue
    val largeValue = max(abs(maxValue), abs(minValue))

    val d1 = floor(log10(abs(segments)))
    val d2 = floor(log10(largeValue))

    val segmentModulus = segments % 10.0.pow(d1)
    val removeMSN = round(segmentModulus / 10.0.pow(d1 - 1))

    var precision = abs(d2.toInt() - d1.toInt()) + 1

    if (removeMSN in 1.0..9.0) precision++

    return precision
}