package zm.experiment.view.plot

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import zm.experiment.model.*
import zm.experiment.view.theme.AppTheme
import zm.experiment.view.theme.AppTheme.custom
import zm.experiment.view.theme.PlotStyle
import zm.experiment.viewmodel.PlotViewModel
import zm.experiment.viewmodel.PlottingMode

@Composable
fun Plotter(view: PlotViewModel) {
    val drawNewData = view.drawNewData
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
            view.newDataDrawn()
        }
    }


    AppTheme {
        Column(modifier = Modifier.padding(25.dp).fillMaxSize()) {
            for (plot in view.plots) {
                Plot(plot, view.traces, view.drawNewData, view.plottingMode, textMeasure, view)
            }

        }
    }
}

@Composable
fun Plot(plot: Plot, traces: List<Trace>, drawNewData: Boolean, plottingMode: PlottingMode, textMeasure: TextMeasurer, view: PlotViewModel) {

    LaunchedEffect(Unit) {
        if (drawNewData) {
            view.newDataDrawn()
        }
    }
    Canvas(modifier = Modifier
        .fillMaxSize()
        .drawWithContent {
            drawContent()
            if (drawNewData) {
                val plotTraces = plot.traces.map { index -> traces[index] }
                plotTraces.fastForEachIndexed { _, trace ->
                    val values = if (plottingMode == PlottingMode.FRAMES) trace.getFramesWindow() else trace.getPlotWindow()
                    val path = generatePath(values.map { it.first }, plot.y.min, plot.y.max, size, padding = 75)
                    drawPath(path, trace.color, style = Stroke(2.dp.toPx()))
                    view.pointsDrawn += trace.size
                }
            }
        }
    ) {
            drawPlot(plot, size, textMeasure)
    }
}


@Composable
fun PlotterOG(view: PlotViewModel) {
    val drawNewData = view.drawNewData
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
            view.newDataDrawn()
        }
    }



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
                if (drawNewData) view.traces.fastForEachIndexed { index, trace ->
                    val values = if (view.plottingMode == PlottingMode.FRAMES) trace.getFramesWindow() else trace.getPlotWindow()
                    val path = generatePath(values.map { it.first }, view.plot.y.min, view.plot.y.max, size, padding = 75)
                    drawPath(path, view.traceColors[index], style = Stroke(2.dp.toPx()))
                    if (index == 0) view.pointsDrawn += view.traces[0].size
//                    println("done drawing")
//                    plot.newDataDrawn()
                }
            }

            ) {
                //drawLine(Color.Red, Offset(0f,0f), Offset(0f, size.height))

//                drawRect(graphAxis, Offset(0f, 0f), size, style = Stroke(2.dp.toPx(), join = StrokeJoin.Round))
//                val step = plot.packetSize / plot.ticks.tickCount.toDouble()
//                repeat(plot.ticks.tickCount) { i ->
//                    val y = mapValue(plot.ticks.getTick(i), plot.ticks.min, plot.ticks.max, size.height.toDouble(), 0.0)
//                    drawLine(gridlines, Offset(0f, y), Offset(size.width, y))
//
//                    val x = mapValue(step * i,0.0, plot.packetSize.toDouble(), 0.0, size.width.toDouble())
//                    drawLine(gridlines, Offset(x, 0f), Offset(x, size.height))
//                }
                drawPlot(view.plot, size, textMeasure)

            }
        }
    }
}



fun DrawScope.drawPlot(plot: Plot, size: Size, textMeasure: TextMeasurer, showXAxis: Boolean = true) {
    drawVerticalAxis(plot.y, size, 75f, textMeasure = textMeasure)
    drawHorizontalAxis(plot.x, size, 75f, textMeasure = textMeasure)
}

fun DrawScope.drawVerticalAxis(axis: Axis, size: Size, padding: Float = 50f, textMeasure: TextMeasurer, style: PlotStyle = PlotStyle.Default) {
    val precision = calculateRequiredPrecision(axis.max, axis.min, axis.segment)
    for (yValue in axis.range()) {//floatRange(axis.min, axis.max, axis.segment)) {
        val y = mapValue(yValue, axis.min, axis.max, size.height - padding, 0f)
        val color = if (yValue == axis.min || yValue == 0f) style.axisColor else style.gridColor
        val textLayout = textMeasure.measure(yValue.formatLabelText(precision), style.textStyle)
        drawText(textLayout, topLeft = Offset(0f - 10f, y - 10f))
        drawLine(color, Offset(padding, y), Offset(size.width, y))
    }

}

fun DrawScope.drawHorizontalAxis(axis: Axis, /*packetSize: Int, pointCount: Int, */size: Size, padding: Float = 50f, textMeasure: TextMeasurer, style: PlotStyle = PlotStyle.Default) {
    //val step = packetSize / axis.ticks.tickCount
    val precision = calculateRequiredPrecision(axis.max, axis.min, axis.segment)
    for (xValue in axis.range()) {//floatRange(axis.min, axis.max, axis.segment)) {
        val x = mapValue(xValue, axis.min, axis.max, 0f + padding, size.width)
        val color = if (xValue == axis.min || xValue == 0f) style.axisColor else style.gridColor
        val textLayout = textMeasure.measure(xValue.formatLabelText(precision), style.textStyle)
        drawText(textLayout, topLeft = Offset(x - 20f, size.height - (padding / 3) * 2))
        drawLine(color, Offset(x, 0f), Offset(x, size.height - padding))
    }
}

fun DrawScope.drawGridLine(value: Float, start: Offset, end: Offset, isMajor: Boolean, style: PlotStyle = PlotStyle.Default) {

}

fun <T: Number> generatePath(values: List<T>, min: T, max: T, size: Size, padding: Int) : Path {
    val path: Path = Path()
    for (i in values.indices) {
        val x = mapValue(i.toDouble(), 0.0, values.size.toDouble(), 0.0 + padding, size.width.toDouble())
        val y = mapValue(values[i].toDouble(), min.toDouble(), max.toDouble(), size.height.toDouble() - padding, 0.0)

        if (i == 0) path.moveTo(x, y)
        else path.lineTo(x, y)
    }
    return path
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

//if (numberOfPlots > 1) {
//    val values = traces[traceIndex].getWindow(plottingMode)
//    val path = generatePath(values.map { it.first }, plot.y.min, plot.y.max, size, padding = 75)
//    drawPath(path, traces[traceIndex].color, style = Stroke(2.dp.toPx()))
//} else {
//    traces.fastForEachIndexed { index, trace ->
//        val values = if (plottingMode == PlottingMode.FRAMES) trace.getFramesWindow() else trace.getPlotWindow()
//        val path = generatePath(values.map { it.first }, plot.y.min, plot.y.max, size, padding = 75)
//        drawPath(path, trace.color, style = Stroke(2.dp.toPx()))
//    }
//}


//fun <T> List<T>.fastForEach(action: (Int, T) -> Unit) {
//    for (index in indices) {
//        action(index, get(index))
//    }
//}
