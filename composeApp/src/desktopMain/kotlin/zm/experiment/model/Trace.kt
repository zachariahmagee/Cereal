package zm.experiment.model

class Trace(
    private val window: Int = 100
) {
    private val capacity= 10000
    private val values1 = DoubleArray(capacity)
    private val values2 = Array<Double?>(capacity) { null }

    private var head = -1
    private var tail = 0

    val size: Int
        get() = if (head == -1) 0 else (head - tail + 1).coerceAtMost(window)

    fun add(value: Double, secondValue: Double? = null) {
        head = (head + 1) % capacity
        values1[head] = value
        values2[head] = secondValue
        if (head - tail >= window) {
            tail = (tail + 1) % capacity
        }
        //println("$tail, $head, $size")
    }

    operator fun get(index: Int): Pair<Double, Double?>? {
        if (index < 0 || index >= size) return null
        val realIndex = (tail + index) % capacity
        return values1[realIndex] to values2[realIndex] // to values2[realIndex] == null
    }

    fun getWindowValue() : List<Double> {
        return (0 until size).mapNotNull { this[it]?.first }
    }

    fun getWindow() : List<Pair<Double, Double?>> {
        return (0 until size).mapNotNull { this[it] }
    }

    fun min(): Double? = getWindowValue().minOfOrNull { it }
    fun max(): Double? = getWindowValue().maxOfOrNull { it }

    fun peaks(threshold: Double) : List<Pair<Int, Double>> {
        return getWindowValue().mapIndexedNotNull { i, v ->
            if (i > 0 && i < size - 1 && v > this[i - 1]!!.first && v > this[i + 1]!!.first && v > threshold) {
                i to v
            } else null
        }
    }

    override fun toString(): String {
        return getWindowValue().joinToString(", ")
    }
}


fun List<Trace>.min() : Double = this.minOfOrNull { it.min() ?: Double.MAX_VALUE } ?: 0.0

fun List<Trace>.max() : Double = this.maxOfOrNull { it.max() ?: Double.MIN_VALUE } ?: 1.0