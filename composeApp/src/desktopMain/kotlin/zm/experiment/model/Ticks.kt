package zm.experiment.model

import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

class Ticks(min: Double, max: Double, tickCount: Int) {
    var tickCount: Int
    var ticks: DoubleArray
    var min = 0.0
    var max = 0.0
    var tickStep = 0.0

    init {
        var tickCount = tickCount
        val range = max - min
        val exp = if (range == 0.0) {
            0.0
        } else {
            floor(log10(range / (tickCount - 1)))
        }
        val scale: Double = 10.0.pow(exp)

        val rawTickStep = (range / (tickCount - 1)) / scale
        for (potentialStep in doubleArrayOf(1.0, 1.5, 2.0, 2.5, 3.0, 4.0, 5.0, 6.0, 8.0, 10.0)) {
            if (potentialStep < rawTickStep) {
                continue
            }

            tickStep = potentialStep * scale
            this.min = tickStep * floor(min / tickStep)
            this.max = this.min + tickStep * (tickCount - 1)
            if (this.max >= max) {
                break
            }
        }

        tickCount -= floor((this.max - max) / tickStep).toInt()
        this.tickCount = tickCount

        ticks = DoubleArray(tickCount)
        for (i in 0 until tickCount) {
            ticks[i] = this.min + i * tickStep
        }
    }

    fun calculate(min: Double, max: Double, tickCount: Int) {
        var tickCount = tickCount
        val range = max - min
        val exp = if (range == 0.0) {
            0.0
        } else {
            floor(log10(range / (tickCount - 1)))
        }
        val scale: Double = 10.0.pow(exp)

        val rawTickStep = (range / (tickCount - 1)) / scale
        for (potentialStep in doubleArrayOf(1.0, 1.5, 2.0, 2.5, 3.0, 4.0, 5.0, 6.0, 8.0, 10.0)) {
            if (potentialStep < rawTickStep) {
                continue
            }

            tickStep = potentialStep * scale
            this.min = tickStep * floor(min / tickStep)
            this.max = this.min + tickStep * (tickCount - 1)
            if (this.max >= max) {
                break
            }
        }

        tickCount -= floor((this.max - max) / tickStep).toInt()
        this.tickCount = tickCount

        ticks = DoubleArray(tickCount)
        for (i in 0 until tickCount) {
            ticks[i] = this.min + i * tickStep
        }
    }

    fun getTick(i: Int): Double {
        return ticks[i]
    }
}