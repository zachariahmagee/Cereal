package zm.experiment.model

class Trace(
    var windowSize: Int = 500,
    private val capacity: Int = 10000
) {
    private val values1 = DoubleArray(capacity)
    private val values2 = Array<Double?>(capacity) { null }

    private var head = -1  // Most recent data index
    private var tail = 0   // Oldest retained data index
    private var endOfLastPacket = 0
    var count = 0
    var lastDrawnIndex = 0  // Helps track updates

    val size: Int
        get() = if (head == -1) 0 else (head - tail + 1).coerceAtMost(capacity)

    val sizeSinceLastPacket: Int
        get() = if (head == -1) 0 else (head - endOfLastPacket).coerceAtMost(windowSize)

    fun add(value: Double, secondValue: Double? = null) {
        head = (head + 1) % capacity
        values1[head] = value
        values2[head] = secondValue
        countValues()
        // If full, move tail forward (preserve window history)
        if ((head + 1) % capacity == tail) {
            tail = (tail + 1) % capacity
        }
    }

    operator fun get(index: Int): Pair<Double, Double?>? {
        if (index < 0 || index >= size) return null
        //val realIndex = (tail + index) % capacity
        var realIndex = (head - windowSize + index)
        if (realIndex < 0) realIndex += capacity
        return values1[realIndex] to values2[realIndex] // to values2[realIndex] == null
    }

    private fun countValues() {
        count++
        if (count >= windowSize) {
            endOfLastPacket = head
            count = 0
        }
    }

    /**
     * Retrieves the data window that should be plotted.
     * This ensures smooth scrolling without dropping old values.
     */
    fun getPlotWindow(): List<Pair<Double, Double?>> {
        val start = maxOf(0, head - windowSize + 1)
        return (start..head).map { i ->
            values1[i % capacity] to values2[i % capacity]
        }
    }

    /**
     * Retrieve all stored data (up to capacity) for debugging or CSV export.
     */
    fun getAllData(): List<Pair<Double, Double?>> {
        return (tail..head).map { i ->
            values1[i % capacity] to values2[i % capacity]
        }
    }

    //fun setWindowSize(window: Int) { windowSize = window }

    fun min(): Double? = getPlotWindow().minOfOrNull { it.first }
    fun max(): Double? = getPlotWindow().maxOfOrNull { it.first }

    fun peaks(threshold: Double): List<Pair<Int, Double>> {
        return getPlotWindow().mapIndexedNotNull { i, (v, _) ->
            if (i > 0 && i < size - 1 && v > this[i - 1]?.first!! && v > this[i + 1]?.first!! && v > threshold) {
                i to v
            } else null
        }
    }

    fun clear() {
        tail = 0
        head = -1
    }

    override fun toString(): String {
        return getPlotWindow().joinToString(", ")
    }
}

class Trace0(
    private val window: Int = 100
) {
    private val capacity= 10000
    private val values1 = DoubleArray(capacity)
    private val values2 = Array<Double?>(capacity) { null }

    private var head = -1
    private var tail = 0
    private var endOfLastPacket = 0
    private var count = 0

    val sizeSinceLastPacket: Int
        get() = if (head == -1) 0 else (head - endOfLastPacket).coerceAtMost(window)

    val size: Int
        get() = if (head == -1) 0 else (head - tail + 1).coerceAtMost(window)

    fun add(value: Double, secondValue: Double? = null) {
        head = (head + 1) % capacity
        values1[head] = value
        values2[head] = secondValue
        count++
        if (count >= window) {
            endOfLastPacket = head
            count = 0
        }
        if (head - tail >= window) {
            tail = (tail + 1) % capacity
        }
        if (head >= capacity - 1) println("Reached Capacity")
        //println("$tail, $head, $size")
    }

    operator fun get(index: Int): Pair<Double, Double?>? {
        if (index < 0 || index >= size) return null
        val realIndex = (tail + index) % capacity
//        var realIndex = (head - window + index)
//        if (realIndex < 0) realIndex += capacity
        return values1[realIndex] to values2[realIndex] // to values2[realIndex] == null
    }

    fun getPlotWindow() : List<Double> {
        return (0 until size).mapNotNull { this[it]?.first }
    }

    fun getWindow() : List<Pair<Double, Double?>> {
        return (0 until size).mapNotNull { this[it] }
    }

    fun min(): Double? = getPlotWindow().minOfOrNull { it }
    fun max(): Double? = getPlotWindow().maxOfOrNull { it }

    fun peaks(threshold: Double) : List<Pair<Int, Double>> {
        return getPlotWindow().mapIndexedNotNull { i, v ->
            if (i > 0 && i < size - 1 && v > this[i - 1]!!.first && v > this[i + 1]!!.first && v > threshold) {
                i to v
            } else null
        }
    }

    fun reset() {
        tail = head
    }

    fun clear() {
        tail = 0
        head = -1
    }

    val isNotEmpty: Boolean
        get() = size > 0


    override fun toString(): String {
        return getPlotWindow().joinToString(", ")
    }
}


fun List<Trace>.min() : Double = this.minOfOrNull { it.min() ?: Double.MAX_VALUE } ?: 0.0

fun List<Trace>.max() : Double = this.maxOfOrNull { it.max() ?: Double.MIN_VALUE } ?: 1.0


class Trace1 (
    private val windowSize: Int = 1000,
    private val isPair: Boolean = false
) {
    private val values1 = ArrayDeque<Double>(windowSize) // Primary values
    private val values2 = if (isPair) ArrayDeque<Double>(windowSize) else null // Optional secondary values

    val size: Int
        get() = values1.size

    fun add(value: Double, secondValue: Double? = null) {
        if (values1.size >= windowSize) {
            values1.removeFirst() // Remove oldest element
            values2?.removeFirst()
        }
        values1.addLast(value)
        secondValue?.let { values2?.addLast(it) }
    }

    // Override indexing operator []
    operator fun get(index: Int): Pair<Double, Double?>? {
        if (index < 0 || index >= size) return null
        return values1[index] to values2?.get(index)
    }

    fun getWindow(): List<Pair<Double, Double?>> = values1.mapIndexed { i, v -> v to values2?.getOrNull(i) }

    fun min(): Double? = values1.minOrNull()
    fun max(): Double? = values1.maxOrNull()

    fun peaks(threshold: Double): List<Pair<Int, Double>>  {
        return values1.mapIndexedNotNull { i, v ->
            if (i > 0 && i < size - 1 && v > values1[i - 1] && v > values1[i + 1] && v > threshold) {
                i to v
            } else null
        }
    }
}