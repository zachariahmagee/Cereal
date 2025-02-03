package zm.experiment.view

import androidx.compose.animation.core.withInfiniteAnimationFrameNanos
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.skia.Bitmap
import zm.experiment.model.Trace
import zm.experiment.model.max
import zm.experiment.model.min
import zm.experiment.view.theme.AppTheme
import zm.experiment.viewmodel.PlotViewModel
import kotlin.math.abs

@Composable
fun Plot(plot: PlotViewModel) {
    AppTheme {
        Canvas(modifier = Modifier.fillMaxSize()) {

        }
    }
}


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

    AppTheme {
        Canvas(modifier = Modifier.fillMaxSize()) {

            if (plot.drawNewData)
                drawIntoCanvas { canvas ->
                val width = size.width
                val height = size.height

                val min = -90.0 //plot.traces.min()
                val max = 10.0 //plot.traces.max()
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
                    canvas.drawPath(path, Paint().apply {
                        color = plot.traceColors[index]
                        style = PaintingStyle.Stroke
                        strokeWidth = 5f
                    })
                    //trace.reset()
                }
            }
            //plot.newDataDrawn()
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

fun drawPlot(traces: List<Trace>, bitmap: Bitmap) {
    val canvas = org.jetbrains.skia.Canvas(bitmap)
    canvas.clear(0xFFFFFFFF.toInt())

    val min = traces.min()
    val max = traces.max()
    val window = traces.firstOrNull()?.size ?: return

    traces.forEachIndexed { index, trace ->
        val path = org.jetbrains.skia.Path()
        val paint = org.jetbrains.skia.Paint().apply {
            color = 0xFF0000FF.toInt()
            mode = org.jetbrains.skia.PaintMode.STROKE
            strokeWidth = 5f
        }
//        trace.getWindowValue().forEachIndexed { x, y ->
//            val
//        }
        for (i in 0 until trace.size) {
            val x = mapValue(i.toDouble(), 0.0, window.toDouble(), 0.0, bitmap.width.toDouble())
            val y = mapValue(trace[i]!!.first, min, max, bitmap.height.toDouble(), 0.0)

            if (i == 0) {
                path.moveTo(x.toFloat(), y.toFloat())
            } else {
                path.lineTo(x.toFloat(), y.toFloat())
            }
            //if (index == 0) plot.pointsDrawn++
        }
        canvas.drawPath(path, paint)
    }

}




fun mapValue(value: Double, fromMin: Double, fromMax: Double, toMin: Double, toMax: Double): Double {
//    val outgoing = toMin + (toMax - toMin) * ((value - fromMin) / (fromMax - fromMin))
//    if (outgoing.isNaN()) {
//        println("Error when mapping value = NaN (not a number)")
//    } else if (outgoing == Double.MAX_VALUE || outgoing == Double.MIN_VALUE) {
//        println("Error when mapping value = infinity")
//    }
//    return outgoing

    return ((value - fromMin) / (fromMax - fromMin)) * (toMax - toMin) + toMin
}

fun mapValueTo(value: Float, start1: Float, stop1: Float, start2: Float, stop2: Float) : Float {
    val outgoing = start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1))
    return outgoing
}

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
