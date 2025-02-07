package zm.experiment.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import zm.experiment.model.Axis
import zm.experiment.model.Plot
import zm.experiment.model.max
import zm.experiment.model.min
import zm.experiment.view.theme.AppTheme
import zm.experiment.view.theme.AppTheme.custom
import zm.experiment.view.theme.PlotStyle
import zm.experiment.viewmodel.PlotViewModel
import zm.experiment.viewmodel.PlottingMode

@Composable
fun Plot(plot: PlotViewModel) {
    AppTheme {
        Canvas(modifier = Modifier.fillMaxSize()) {

        }
    }
}

// Uses drawable traces and "refreshDrawableTraces
//@Composable
//fun Plotter0(plot: PlotViewModel) {
//    val redrawTrigger = plot.redrawTrigger
//    val last = remember { mutableStateOf(0L) }
//    val elapsed = remember { mutableStateOf(0L)}
//
//    if (last.value == 0L) last.value = System.currentTimeMillis()
//    else {
//        elapsed.value = System.currentTimeMillis() - last.value
//        last.value = System.currentTimeMillis()
//        //println("Elapsed time: ${elapsed.value}")
//    }
//
//    AppTheme {
//        Canvas(modifier = Modifier.fillMaxSize()) {
//
//            val width = size.width
//            val height = size.height
//
//            plot.drawableTraces.forEachIndexed { index, list ->
//                val path = Path()
//
//                val min = list.minOf { it }
//                val max = list.maxOf { it }
//                println("$list")
//                for (i in list.indices) {
//
//                    val x = mapValue(i.toDouble(), 0.0, list.size.toDouble(), 0.0, width.toDouble())
//                    val y = mapValue(list[i], min, max, height.toDouble(), 0.0)
//
//                    if (i == 0) {
//                        path.moveTo(x.toFloat(), y.toFloat())
//                    } else {
//                        path.lineTo(x.toFloat(), y.toFloat())
//                    }
//                }
//                drawPath(path, color = plot.traceColors[index], style = Stroke(width = 5.dp.toPx()))
//            }
//        }
//        Text(text = redrawTrigger.toString(), color = Color.White)
//    }
//}


// Works as best as I have gotten anything to work
@Composable
fun PlotterGOOD(plot: PlotViewModel) {
    val drawNewData = plot.drawNewData
    val padding: Int = 25
    //val traces by remember { derivedStateOf { plot.traces }}

    val last = remember { mutableStateOf(0L) }
    val elapsed = remember { mutableStateOf(0L)}

    if (last.value == 0L) last.value = System.currentTimeMillis()
    else {
        elapsed.value = System.currentTimeMillis() - last.value
        last.value = System.currentTimeMillis()
       // println("Elapsed time: ${elapsed.value}")
    }

//    val redrawTrigger2 by remember { mutableStateOf(0) }
//
    LaunchedEffect(Unit) {
        if (drawNewData) {
            plot.newDataDrawn()
        }
//        while(true) {
//            withInfiniteAnimationFrameNanos { frameTimeNanos ->
//                println("Frame drawn at: ${frameTimeNanos / 1_000_000}, ${elapsed.value}, ${plot.drawNewData}")
//
//            }
//        }
    }

    val min by remember { mutableStateOf(plot.traces.min() ?: 0.0) }
    val max by remember { mutableStateOf(plot.traces.max() ?: 500.0) }
    plot.ticks(min, max)
    val ticks by remember { mutableStateOf(plot.ticks)}

    AppTheme {
        Canvas(modifier = Modifier
            .padding(25.dp)
            .fillMaxSize()
        ) {
            if (plot.drawNewData)
                drawIntoCanvas { canvas ->

                val width = size.width
                val height = size.height
                val min = plot.traces.min()
                val max = plot.traces.max()

                plot.traces.fastForEach { index, trace ->
                    val path = Path()

                    for (i in 0 until trace.size) {
                        val x = mapValue(i.toDouble(), 0.0, plot.packetSize.toDouble()/*trace.size.toDouble()*/, 0.0, width.toDouble())
                        val y = mapValue(trace[i]!!.first, min, max, height.toDouble(), 0.0)

                        if (i == 0) {
                            path.moveTo(x.toFloat(), y.toFloat())
                        } else {
                            path.lineTo(x.toFloat(), y.toFloat())
                        }
                        if (index == 0) plot.pointsDrawn++
                    }
                    //val path = generatePath(trace.getWindowValue(), min, max, size)
                    canvas.drawPath(path, Paint().apply {
                        color = plot.traceColors[index]
                        style = PaintingStyle.Stroke
                        strokeWidth = 5f
                    })
                }
            }
        }
    }
}

fun <T: Number> generatePath(values: List<T>, min: T, max: T, size: Size, padding: Int) : Path {
    val path: Path = Path()
    for (i in values.indices) {
        val x = mapValue(i.toDouble(), 0.0, values.size.toDouble(), 0.0 + padding, size.width.toDouble() - padding)
        val y = mapValue(values[i].toDouble(), min.toDouble(), max.toDouble(), size.height.toDouble() - padding, 0.0 + padding)

        if (i == 0) path.moveTo(x, y)
        else path.lineTo(x, y)
    }
    return path
}


@Composable
fun Plotter(plot: PlotViewModel) {
    val drawNewData = plot.drawNewData
    //val traces by remember { derivedStateOf { plot.traces }}
    val textMeasure = rememberTextMeasurer()
    val last = remember { mutableStateOf(0L) }
    val elapsed = remember { mutableStateOf(0L)}

    if (last.value == 0L) last.value = System.currentTimeMillis()
    else {
        elapsed.value = System.currentTimeMillis() - last.value
        last.value = System.currentTimeMillis()
        // println("Elapsed time: ${elapsed.value}")
    }

    LaunchedEffect(Unit) {
        if (drawNewData) {
            plot.newDataDrawn()
        }
    }



    AppTheme {
        val graphAxis: Color = custom.grey125
        val gridlines: Color = custom.grey190


        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(
                modifier = Modifier
                    .padding(25.dp)
                    .fillMaxSize()
                //.graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
            .drawWithContent {
//                plot.ticks(plot.traces.min(), plot.traces.max())
//                min = plot.ticks.min
//                max = plot.ticks.min//ticks.max
//                println("$min, $max")
                drawContent()
                if (drawNewData) plot.traces.fastForEach { index, trace ->
                    val values = if (plot.plottingMode == PlottingMode.FRAMES) trace.getFramesWindow() else trace.getPlotWindow()
                    val path = generatePath(values.map { it.first }, plot.ticks.min, plot.ticks.max, size, padding = 0)
                    drawPath(path, plot.traceColors[index], style = Stroke(2.dp.toPx()))
                    if (index == 0) plot.pointsDrawn += plot.traces[0].size
//                    println("done drawing")
//                    plot.newDataDrawn()
                }
            }

            ) {
                //drawLine(Color.Red, Offset(0f,0f), Offset(0f, size.height))

                drawRect(graphAxis, Offset(0f, 0f), size, style = Stroke(2.dp.toPx(), join = StrokeJoin.Round))
                val step = plot.packetSize / plot.ticks.tickCount.toDouble()
                repeat(plot.ticks.tickCount) { i ->
                    val y = mapValue(plot.ticks.getTick(i), plot.ticks.min, plot.ticks.max, size.height.toDouble(), 0.0)
                    drawLine(gridlines, Offset(0f, y), Offset(size.width, y))

                    val x = mapValue(step * i,0.0, plot.packetSize.toDouble(), 0.0, size.width.toDouble())
                    drawLine(gridlines, Offset(x, 0f), Offset(x, size.height))
                }
//                for (p in plot.plots) {
//
//                }

            }
        }
    }
}



fun DrawScope.drawPlot(plot: Plot, size: Size, textMeasure: TextMeasurer, showXAxis: Boolean = true) {
    var new = size - 5
}

fun DrawScope.drawVerticalAxis(axis: Axis, size: Size, padding: Float = 50f, textMeasure: TextMeasurer, style: PlotStyle = PlotStyle.Default) {

    for (yValue in floatRange(axis.min, axis.max, axis.segment)) {
        val y = mapValue(yValue, axis.min, axis.max, size.height - padding, 0f)
        val color = if (yValue == axis.min || yValue == 0f) style.axisColor else style.gridColor
        val textLayout = textMeasure.measure(yValue.toString(), style.textStyle)
        drawText(textLayout, topLeft = Offset(5f, y))
        drawLine(color, Offset(padding, y), Offset(size.width, y))
    }

}

fun DrawScope.drawHorizontalAxis(axis: Axis, packetSize: Int, pointCount: Int, size: Size, textMeasure: TextMeasurer, style: PlotStyle = PlotStyle.Default) {
    for (xValue in floatRange(axis.min, axis.max, axis.segment)) {


    }
}

fun DrawScope.drawGridLine(value: Float, start: Offset, end: Offset, isMajor: Boolean, style: PlotStyle = PlotStyle.Default) {

}

fun floatRange(start: Float, end: Float, step: Float)  = sequence {
    var current = start
    while (current <= end) {
        yield(current)
        current += step
    }
}


operator fun Size.minus(value: Int) : Size {
    val height = this.height - value
    val width = this.width - value
    return Size(width, height)
}

fun <T> mapValue(value: T, fromMin: T, fromMax: T, toMin: T, toMax: T) : Float where T : Number, T : Comparable<T>{
    return ((value.toFloat() - fromMin.toFloat()) / (fromMax.toFloat() - fromMin.toFloat())) * (toMax.toFloat() - toMin.toFloat()) + toMin.toFloat()
}


fun <T> List<T>.fastForEach(action: (Int, T) -> Unit) {
    for (index in indices) {
        action(index, get(index))
    }
}
