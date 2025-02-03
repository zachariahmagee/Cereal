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
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.dp
import zm.experiment.model.max
import zm.experiment.model.min
import zm.experiment.view.theme.AppTheme
import zm.experiment.view.theme.AppTheme.custom
import zm.experiment.viewmodel.PlotViewModel

@Composable
fun Plot(plot: PlotViewModel) {
    AppTheme {
        Canvas(modifier = Modifier.fillMaxSize()) {

        }
    }
}

// Uses drawable traces and "refreshDrawableTraces
@Composable
fun Plotter0(plot: PlotViewModel) {
    val redrawTrigger = plot.redrawTrigger
    val last = remember { mutableStateOf(0L) }
    val elapsed = remember { mutableStateOf(0L)}

    if (last.value == 0L) last.value = System.currentTimeMillis()
    else {
        elapsed.value = System.currentTimeMillis() - last.value
        last.value = System.currentTimeMillis()
        //println("Elapsed time: ${elapsed.value}")
    }

    AppTheme {
        Canvas(modifier = Modifier.fillMaxSize()) {

            val width = size.width
            val height = size.height

            plot.drawableTraces.forEachIndexed { index, list ->
                val path = Path()

                val min = list.minOf { it }
                val max = list.maxOf { it }
                println("$list")
                for (i in list.indices) {

                    val x = mapValue(i.toDouble(), 0.0, list.size.toDouble(), 0.0, width.toDouble())
                    val y = mapValue(list[i], min, max, height.toDouble(), 0.0)

                    if (i == 0) {
                        path.moveTo(x.toFloat(), y.toFloat())
                    } else {
                        path.lineTo(x.toFloat(), y.toFloat())
                    }
                }
                drawPath(path, color = plot.traceColors[index], style = Stroke(width = 5.dp.toPx()))
            }
        }
        Text(text = redrawTrigger.toString(), color = Color.White)
    }
}


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

    val last = remember { mutableStateOf(0L) }
    val elapsed = remember { mutableStateOf(0L)}

    if (last.value == 0L) last.value = System.currentTimeMillis()
    else {
        elapsed.value = System.currentTimeMillis() - last.value
        last.value = System.currentTimeMillis()
        // println("Elapsed time: ${elapsed.value}")
    }

//    LaunchedEffect(Unit) {
//        if (drawNewData) {
//            plot.newDataDrawn()
//        }
//    }

//    var min by remember { mutableStateOf(plot.traces.min() ?: 0.0) }
//    var max by remember { mutableStateOf(plot.traces.max() ?: 500.0) }
//    println("before: $min, $max")
//    plot.ticks(plot.traces.min(), plot.traces.max())
//    min = plot.ticks.min
//    max = plot.ticks.min
//    println("after: $min, $max")
//    val ticks by remember { mutableStateOf(plot.ticks)}
//    var min: Double
//    var max: Double

    AppTheme {
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
                plot.traces.fastForEach { index, trace ->
                    val path = generatePath(trace.getWindowValue(), plot.ticks.min, plot.ticks.max, size, padding = 0)
                    drawPath(path, plot.traceColors[index], style = Stroke(5.dp.toPx()))
                    if (index == 0) plot.pointsDrawn += plot.traces[0].size
                }
            }

            ) {
                //drawLine(Color.Red, Offset(0f,0f), Offset(0f, size.height))

                drawRect(Color(119,119,119), Offset(0f, 0f), size, style = Stroke(2.dp.toPx(), join = StrokeJoin.Round))

            }
        }
    }
}



//@Composable
//fun Plotter2(plot: PlotViewModel) {
//    val drawNewData = plot.drawNewData
//
//    val last = remember { mutableStateOf(0L) }
//    val elapsed = remember { mutableStateOf(0L)}
//
//    if (last.value == 0L) last.value = System.currentTimeMillis()
//    else {
//        elapsed.value = System.currentTimeMillis() - last.value
//        last.value = System.currentTimeMillis()
//    }
//
//    LaunchedEffect(Unit) {
//        if (drawNewData) {
//            plot.newDataDrawn()
//        }
//    }
//
//    Canvas(modifier = Modifier.fillMaxSize()) {
//        val min = plot.traces.min()
//        val max = plot.traces.max()
//        plot.traces.forEachIndexed { index, trace ->
//            PlotTrace(trace, plot.traceColors[index], size)
//        }
//    }
//}
//
//
//@Composable
//fun PlotTrace(trace: Trace, traceColor: Color, size: Size, min: Double = trace.min()!!, max: Double = trace.max()!!) {
//    Spacer(modifier = Modifier.fillMaxSize().drawBehind {
//        drawIntoCanvas { canvas ->
//
//            val path = Path().apply {
//                if (trace.isNotEmpty) {
//
//                    for (i in 0 until trace.size) {
//                        val x = mapValue(i.toDouble(), 0.0, trace.size.toDouble(), 0.0, size.width.toDouble())
//                        val y = mapValue(trace[i]!!.first, min, max, size.height.toDouble(), 0.0)
//
//                        if (i == 0) {
//                            moveTo(x.toFloat(), y.toFloat())
//                        } else {
//                            lineTo(x.toFloat(), y.toFloat())
//                        }
//                        //if (index == 0) plot.pointsDrawn++
//                    }
//                }
//            }
//            canvas.drawPath(path, Paint().apply {
//                color = traceColor
//                style = PaintingStyle.Stroke
//                strokeWidth = 5f
//            })
//        }
//    })
//
//}


fun <T> mapValue(value: T, fromMin: T, fromMax: T, toMin: T, toMax: T) : Float where T : Number, T : Comparable<T>{
    return ((value.toFloat() - fromMin.toFloat()) / (fromMax.toFloat() - fromMin.toFloat())) * (toMax.toFloat() - toMin.toFloat()) + toMin.toFloat()
}

//fun mapValue(value: Double, fromMin: Double, fromMax: Double, toMin: Double, toMax: Double): Double {
////    val outgoing = toMin + (toMax - toMin) * ((value - fromMin) / (fromMax - fromMin))
////    if (outgoing.isNaN()) {
////        println("Error when mapping value = NaN (not a number)")
////    } else if (outgoing == Double.MAX_VALUE || outgoing == Double.MIN_VALUE) {
////        println("Error when mapping value = infinity")
////    }
////    return outgoing
//
//    return ((value - fromMin) / (fromMax - fromMin)) * (toMax - toMin) + toMin
//}

//fun mapValueTo(value: Float, start1: Float, stop1: Float, start2: Float, stop2: Float) : Float {
//    val outgoing = start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1))
//    return outgoing
//}

//private fun DrawScope.drawPath(
//    path: List<Offset>,
//    color: Color,
//    thickness: Float = 10f
//) {
//    val interpolatedPath = Path().apply {
//        if (path.isNotEmpty()) {
//            moveTo(path.first().x, path.first().y)
//
//            for (i in 1 .. path.lastIndex) {
//                val from = path[i - 1]
//                val to = path[i]
//                val dx = abs(from.x - to.x)
//                val dy = abs(from.y - to.y)
//                lineTo(from.x, to.y)
//                lineTo(to.x, to.y)
//
//
//            }
//        }
//    }
//    drawPath(path = path, color = color, thickness = thickness)
//}

fun <T> List<T>.fastForEach(action: (Int, T) -> Unit) {
    for (index in indices) {
        action(index, get(index))
    }
}
