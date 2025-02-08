package zm.experiment.model

import kotlin.test.Test



class CalculateRequiredPrecisionTest {
    companion object {
        val sampleData = listOf(
            Axis(0f, 500f, 100f),
            Axis(-10000f, 10000f, 1000f),
            Axis(0f,1f, 0.25f),
            Axis(0f, 1_000_000_000f, 100_000_000f)
        )
    }

    @Test
    fun testRequiredPrecisionCalculation() {
        val results = mutableListOf<Int>()
        sampleData.forEach { axis ->
            results.add(calculateRequiredPrecision(axis.max, axis.min, axis.segment))

        }
        results.forEach { println(it)}


    }

}