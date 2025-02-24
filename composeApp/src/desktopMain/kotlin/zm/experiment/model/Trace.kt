package zm.experiment.model

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import zm.experiment.viewmodel.PlottingMode
import kotlin.math.max
import kotlin.math.min

class Trace(
    var windowSize: Int = 500,
    private val capacity: Int = 10000,
    var color: Color = Color.Black,
) {
    private val values1 = DoubleArray(capacity)
    private val values2 = Array<Double?>(capacity) { null }

    var label: String = ""
    var isVisible = true
    private var head = -1  // Most recent data index
    private var tail = 0   // Oldest retained data index
    private var endOfLastPacket = 0 // last drawn head
    var count = 0 // points since last drawn head
    var lastDrawnIndex = 0  // Helps track updates

    var max: Double = 0.0
    var min: Double = 0.0

    val size: Int
        get() = if (head == -1) 0 else (head - tail + capacity) % capacity + 1//(head - tail + 1).coerceAtMost(capacity)

    val sizeSinceLastPacket: Int
        get() = if (head == -1) 0 else (head - endOfLastPacket + capacity) % capacity + 1//(head - endOfLastPacket + 1).coerceAtMost(windowSize)

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
        return values1[realIndex % capacity] to values2[realIndex % capacity] // to values2[realIndex] == null
    }

    private fun countValues() {
        count++
        if (count > windowSize - 1) { // - 1 ??
            endOfLastPacket = head
            count = 0
        }
    }

    /**
     * Retrieves the data window that should be plotted.
     * This ensures smooth scrolling without dropping old values.
     */
    fun getPlotWindow(): List<Pair<Double, Double>> {
        var begin = head - windowSize + 1
        if (begin < 0) begin += capacity

        val end = if (begin > head) capacity + head else head
        var index: Double = 0.0
        return (begin..end).map { i ->
            val x = values2[i % capacity] ?: index++
            values1[i % capacity] to x// if (values2[i % capacity] == null) index++ else values2[i % capacity]
        }
//        val start = maxOf(0, head - windowSize + 1) // + 1 ??
//        return (start..head).map { i ->
//            values1[i % capacity] to values2[i % capacity]
//        }
    }

    fun getFramesWindow(): List<Pair<Double, Double>> {
        // TODO: Does this need a + 1 ???
//        val start = maxOf(0, endOfLastPacket - windowSize + 1)

        var begin = endOfLastPacket - windowSize + 1
        if (begin < 0) begin += capacity

        val end = if (begin > endOfLastPacket) capacity + endOfLastPacket else endOfLastPacket
            //println("frame = $begin .. $end ${(max(begin, end) - min(begin, end)) + 1}")
        var index: Double = 0.0
        return (begin..end).map { i ->
            val x: Double = values2[i % capacity] ?: index++//if (values2[i % capacity] == null) index++ else values2[i % capacity]
            values1[i % capacity] to x
        }

//
//        return (start..endOfLastPacket).map { i ->
//            values1[i % capacity] to values2[1 % capacity]
//        }
    }

    fun generatePath(plot: Plot, size: Size, mode: PlottingMode, padding: Int = 75): Path {
        val path: Path = Path()
        val values = if (mode == PlottingMode.FRAMES) getFramesWindow() else getPlotWindow()//getWindow(mode)
        for ((y, x) in values) {
            val offset = plot.toOffset(x.toFloat(), y.toFloat(), size, padding)
            if (x == 0.0) path.moveTo(offset.x, offset.y)
            else path.lineTo(offset.x, offset.y)
        }
        return path
    }

    fun getWindow(mode: PlottingMode = PlottingMode.SCROLLING): List<Pair<Double, Double>> = if (mode == PlottingMode.SCROLLING) getPlotWindow() else getFramesWindow()

    fun getWindow(test: () -> Boolean) : List<Pair<Double,Double>> = if (test()) getPlotWindow() else getFramesWindow()

    /**
     * Retrieve all stored data (up to capacity) for debugging or CSV export.
     */
    fun getAllData(): List<Pair<Double, Double?>> {
        return (tail..head).map { i ->
            values1[i % capacity] to values2[i % capacity]
        }
    }

    //fun setWindowSize(window: Int) { windowSize = window }
    // TODO: Deal with min/max of getFramesWindow
    fun min(): Double? = getPlotWindow().minOfOrNull { it.first }
    fun max(): Double? = getPlotWindow().maxOfOrNull { it.first }

    fun min(test: ()->Boolean): Double {
        val window = if (test()) getPlotWindow() else getFramesWindow()
        min = window.minOf { it.first }
        return min
    }

    fun max(test: () -> Boolean): Double {
        val window = if (test()) getPlotWindow() else getFramesWindow()
        max = window.maxOf { it.first }
        return max
    }

    fun peaks(threshold: Double): List<Pair<Int, Double>> {
        return getPlotWindow().mapIndexedNotNull { i, (v, _) ->
            if (i > 0 && i < size - 1 && v > this[i - 1]?.first!! && v > this[i + 1]?.first!! && v > threshold) {
                i to v
            } else null
        }
    }

    fun nextPeak(threshold: Double) = sequence {
        var lastCheckedIndex = 0  // Keep track of where we left off

        while (true) { // Infinite sequence, stops when no peaks are found
            val window = getFramesWindow() // Get the latest window

            for (i in lastCheckedIndex until window.size - 1) {
                if (i > 0 && i < window.size - 1 &&
                    window[i].first > threshold &&
                    window[i].first >= window[i - 1].first &&
                    window[i].first > window[i + 1].first) {

                    yield(i to window[i].first) // Emit the next peak
                    lastCheckedIndex = i + 1 // Move forward to avoid duplicates
                    break // Exit loop and wait for the next `next()` call
                }
            }
        }
    }

    fun findNextPeak(currentIndex: Int = 0, threshold: Double = -30.0/*(max - min) / 2*/) : Pair<Int, Float> {
        val window = getFramesWindow()//.map { it.first.toFloat() }
        for (i in currentIndex + 1 until window.size - 1) {
            if (i > 0 && i < window.size - 1 &&
                window[i].first > threshold &&
                window[i].first >= window[i - 1].first &&
                window[i].first > window[i + 1].first) {
                println("found a peak ${i}, ${window[i].first}")
                return i to window[i].first.toFloat() // Emit the next peak
            }
        }
        println("Didnt find one")
        for (i in 0 until currentIndex) {
            if (i > 0 && i < window.size - 1 &&
                window[i].first > threshold &&
                window[i].first >= window[i - 1].first &&
                window[i].first > window[i + 1].first) {

                return i to window[i].first.toFloat() // Emit the next peak
            }
        }
        println("Still no")
        return currentIndex to window[currentIndex].first.toFloat()
    }

//    fun findPreviousPeak(currentIndex: Int = 0, threshold: Double = (max - min) / 2) : Pair<Int, Float> {
//        val window = getFramesWindow()//.map { it.first.toFloat() }
//        return window[currentIndex]
//        var i: Int = currentIndex
//        for (i in i >= 0; i--) {
//            if (i > 0 && i < window.size - 1 &&
//                window[i].first > threshold &&
//                window[i].first >= window[i - 1].first &&
//                window[i].first > window[i + 1].first) {
//
//                return i to window[i].first.toFloat() // Emit the next peak
//            }
//        }
//        for (i in 0 until currentIndex) {
//            if (i > 0 && i < window.size - 1 &&
//                window[i].first > threshold &&
//                window[i].first >= window[i - 1].first &&
//                window[i].first > window[i + 1].first) {
//
//                return i to window[i].first.toFloat() // Emit the next peak
//            }
//        }
//        return currentIndex to window[currentIndex].first.toFloat()
//    }



    fun List<Float>.isPeak(i: Int, threshold: Double): Boolean {
        return (i > 0 && i < this.size - 1 &&
            this[i] > threshold &&
            this[i] >= this[i - 1] &&
            this[i]> this[i + 1]) //{

            //eturn i to this[i] // Emit the next peak
        //}
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