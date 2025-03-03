package zm.experiment.view.plot

import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import zm.experiment.model.*
import zm.experiment.view.icon.drawMarker
import zm.experiment.view.theme.AppTheme
import zm.experiment.view.theme.PlotStyle
import zm.experiment.viewmodel.PlotViewModel
import zm.experiment.viewmodel.PlottingMode
import kotlin.math.roundToInt

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Plotter(view: PlotViewModel) {
    val drawNewData = view.drawNewData
    val drawMarkers = view.drawMarkers
    var size by remember { mutableStateOf( Size.Zero ) }
    val density = LocalDensity.current
    val widthInDp = with(density) { size.width.toDp() }
    val heightInDp = with(density) { size.height.toDp() }

    val labelsHeight = with(density) { 20.dp.toPx() }

    val textMeasure = rememberTextMeasurer()
    val last = remember { mutableStateOf(0L) }
    val elapsed = remember { mutableStateOf(0L) }

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
    val selected = AppTheme.custom.selected
    AppTheme {
        Column(modifier = Modifier
            .padding(top = 25.dp, end = 25.dp, start = 25.dp, bottom = 5.dp)
            .fillMaxSize()
            .onGloballyPositioned { layoutCoordinates ->
                size = Size(
                    layoutCoordinates.size.width.toFloat(),
                    layoutCoordinates.size.height.toFloat() - labelsHeight
                )
            }
        ) {
            view.plots.fastForEachIndexed { index, plot ->
                val style: PlotStyle = if (index < view.plots.size - 1) PlotStyle(showHorizontalLabels = true) else PlotStyle.Default
                val possibleHeight = heightInDp / view.plots.size
                val height = if (possibleHeight > 75.dp) possibleHeight else 75.dp
                Box(modifier = Modifier.fillMaxWidth().height(height)) {
                    Plot(
                        plot,
                        view.traces,
                        view.drawNewData,
                        view.plottingMode,
                        textMeasure,
                        view,
                        modifier = Modifier.height(height),
                        style
                    )
                    Markers(plot, size, view)
//                    if (view.drawMarkers) {
//                        view.markers.fastForEach { marker ->
//                            var locked: Boolean by remember { mutableStateOf(false) }
////                            view.updateMarkerValue(marker.id)
//                            view.setMarkerOffset(marker.id, plot.toOffset(marker.index.toFloat(), marker.value, size, style.padding.toInt()), style.markerSize)
//                            val offset = Offset(marker.offset.x - style.markerSize.width / 2, marker.offset.y - style.markerSize.height)
//                            Canvas(modifier = Modifier
//                                .layout { measurable, constraints ->
//                                    val placeable = measurable.measure(constraints)
//                                    layout(constraints.maxWidth, constraints.minWidth) {
//                                        //placeable.place(x = marker.zeroOffset.x.toInt(), y = marker.zeroOffset.y.toInt())
//                                        placeable.place(x = offset.x.toInt(), offset.y.toInt())
//                                    }
//                                }
//                                .height(style.markerSize.height.dp)
//                                .width(style.markerSize.width.dp)
//                                .drawWithContent {
//                                    view.redrawTrigger++
//                                    //view.setMarkerOffset(marker.id, plot.toOffset(marker.index.toFloat(), marker.value, size, style.padding.toInt()))
//                                    if (marker.id == view.selectedMarkerID) {
//                                        val x = marker.offset.x - style.markerSize.width / 2
//                                        val y = marker.offset.y - style.markerSize.height - 12f
//
//
//                                        drawRect(selected, Offset(0f, -10f),//Offset.Zero
//                                            //(x, y)
//                                            Size(50f, 10f))
//                                        drawRect(Color.Black, Offset(0f, -10f), Size(50f, 10f), style = Stroke())
//                                    }
//                                    drawContent()
////                                    drawMarker(marker.offset, view.traceColors[marker.trace], 50f, 50f)
//                                }
//                                .pointerInput(marker.id) {
//                                    detectDragGestures(
//                                        onDragStart = {
//                                            locked = true
//                                        },
//                                        onDragEnd = {
//                                            locked = false
//                                        },
//                                        onDragCancel = {
//                                            locked = false
//                                        },
//                                        onDrag = { change, amount ->
//                                            change.consume()
//                                            if (locked) {
//                                                view.dragMarker(
//                                                    marker.id,
//                                                    amount.x,
//                                                    plot,
//                                                    size,
//                                                )
//                                            }
//                                        }
//                                    )
//                                }
//                            ) {
//                                view.redrawTrigger++
//                                drawMarker(marker,
//                                    Offset.Zero, //marker.offset,
//                                    view.traceColors[marker.trace], style.markerSize.width, style.markerSize.height)
//                            }
//                        }
//                    }
                }
                if (view.redrawTrigger++ == 1000) view.redrawTrigger = 0
            }
            Labels(view)
        }
    }
}

fun DrawScope.drawMarker(marker: Marker, point: Offset, color: Color, width: Float = 30f, height: Float = 30f, style: DrawStyle = Fill) {
    val smw = width / 10f
    val smh = height / 10f
    val path = Path().apply {
        moveTo(smw, point.y + smw)
        lineTo(width - smw, smw)
        lineTo(width / 2, height)
//        moveTo(point.x, point.y)
//        lineTo(point.x - width / 2 + smw, point.y - height + smh * 2)
//        lineTo(point.x + width / 2 - smw, point.y - height + smh * 2)
        close()
    }
    drawPath(path = path, color = color, style = style)
    drawPath(path = path, color = Color.Black, style = Stroke())
//    drawText
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Plot(plot: Plot, traces: List<Trace>, drawNewData: Boolean, plottingMode: PlottingMode, textMeasure: TextMeasurer, view: PlotViewModel, modifier: Modifier = Modifier, style: PlotStyle = PlotStyle.Default) {

    var size by remember { mutableStateOf( Size.Zero ) }
    LaunchedEffect(Unit) {
        if (drawNewData) {
            view.newDataDrawn()
        }

    }
        Canvas(modifier = Modifier.fillMaxSize()
            .onGloballyPositioned { layoutCoordinates ->
                size = Size(
                    layoutCoordinates.size.width.toFloat(),
                    layoutCoordinates.size.height.toFloat()
                )
            }
            .onPointerEvent(PointerEventType.Move) { event ->
                val position = event.changes.first().position
            }
            .drawWithContent {
                drawContent()
                if (drawNewData) {
                    val plotTraces = plot.traces.map { index -> traces[index] }
                    plotTraces.fastForEachIndexed { _, trace ->
                        if (trace.isVisible) {
                            val path = trace.generatePath(plot, size, plottingMode, 75)
                            drawPath(path, trace.color, style = Stroke(2.dp.toPx()))
                            view.pointsDrawn += trace.size
                        }
                    }
                }

            }
        ) {
            drawPlot(plot, size, textMeasure, style)
        }
}

@Composable
fun Markers(plot: Plot, size: Size, view: PlotViewModel) {
    LaunchedEffect(key1 = view.redrawTrigger) {
        view.redrawTrigger++
    }
    if (view.drawMarkers) {
        val selected = AppTheme.custom.selected
        view.markers.fastForEach { marker ->
            var locked: Boolean by remember { mutableStateOf(false) }
            //view.updateMarkerValue(marker.id)
            view.setMarkerOffset(marker.id, plot.toOffset(marker.index.toFloat(), marker.value/*view.traces[marker.trace][marker.index, view.frames]!!.first.toFloat()*/, size, plot.style.padding.toInt()), plot.style.markerSize)
            val offset = Offset(marker.offset.x - plot.style.markerSize.width / 2, marker.offset.y - plot.style.markerSize.height)
            Canvas(modifier = Modifier
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    layout(constraints.maxWidth, constraints.minWidth) {
                        //placeable.place(x = marker.zeroOffset.x.toInt(), y = marker.zeroOffset.y.toInt())
                        placeable.place(x = offset.x.toInt(), offset.y.toInt())
                    }
                }
                .height(plot.style.markerSize.height.dp)
                .width(plot.style.markerSize.width.dp)
                .drawWithContent {
                    view.redrawTrigger++
                    //view.setMarkerOffset(marker.id, plot.toOffset(marker.index.toFloat(), marker.value, size, style.padding.toInt()))
                    if (marker.id == view.selectedMarkerID) {
                        val x = marker.offset.x - plot.style.markerSize.width / 2
                        val y = marker.offset.y - plot.style.markerSize.height - 12f


                        drawRect(selected, Offset(0f, -10f),//Offset.Zero
                            //(x, y)
                            Size(50f, 10f))
                        drawRect(Color.Black, Offset(0f, -10f), Size(50f, 10f), style = Stroke())
                    }
                    drawContent()
//                                    drawMarker(marker.offset, view.traceColors[marker.trace], 50f, 50f)
                }
                .pointerInput(marker.id) {
                    detectDragGestures(
                        onDragStart = {
                            locked = true
                        },
                        onDragEnd = {
                            locked = false
                        },
                        onDragCancel = {
                            locked = false
                        },
                        onDrag = { change, amount ->
                            change.consume()
                            if (locked) {
                                view.dragMarker(
                                    marker.id,
                                    amount.x,
                                    plot,
                                    size,
                                )
                            }
                        }
                    )
                }
            ) {
                view.redrawTrigger++
                drawMarker(marker,
                    Offset.Zero, //marker.offset,
                    view.traceColors[marker.trace], plot.style.markerSize.width, plot.style.markerSize.height)
            }
        }
    }
}
@Composable
fun Markers(drawMarkers: Boolean, markers: List<Marker>, plot: Plot, view: PlotViewModel) {
    val selected = AppTheme.custom.selected
    if (drawMarkers) {
        markers.fastForEach { marker ->
            Canvas(modifier = Modifier
                .drawWithContent {
                    view.redrawTrigger++
                    view.setMarkerOffset(marker.id, plot.toOffset(marker.index.toFloat(), marker.value, size, plot.style.padding.toInt()))
                    if (marker.id == view.selectedMarkerID) {
                        val y = marker.offset.y - 50f - 15f
                        drawRect(selected, Offset(marker.offset.x, y), Size(50f, 10f))
                    }
                    drawMarker(marker.offset, view.traceColors[marker.trace], 50f, 50f)
                }
                .graphicsLayer {
//                        updateTransition(view.redrawTrigger)
                    shadowElevation = 0.5f
                }
            ) {
                drawMarker(marker.offset, view.traceColors[marker.trace], 50f, 50f)
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Plot1(plot: Plot, traces: List<Trace>, drawNewData: Boolean, plottingMode: PlottingMode, textMeasure: TextMeasurer, view: PlotViewModel, modifier: Modifier = Modifier, style: PlotStyle = PlotStyle.Default) {
    val selected = AppTheme.custom.selected
    var size by remember { mutableStateOf( Size.Zero ) }
    LaunchedEffect(Unit) {
        if (drawNewData) {
            view.newDataDrawn()
        }

    }
    Box(modifier = modifier.fillMaxWidth()
        .onGloballyPositioned { layoutCoordinates ->
            size = Size(
                layoutCoordinates.size.width.toFloat(),
                layoutCoordinates.size.height.toFloat()
            )
        }
        .onPointerEvent(PointerEventType.Move) { event ->
            val position = event.changes.first().position
        }
        .onPointerEvent(PointerEventType.Press) { event ->

        }
//        .onClick {}
    ) {
        Canvas(modifier = Modifier.fillMaxSize()
            .onGloballyPositioned { layoutCoordinates ->
                size = Size(
                    layoutCoordinates.size.width.toFloat(),
                    layoutCoordinates.size.height.toFloat()
                )
            }
            .drawWithContent {
                drawContent()
                if (drawNewData) {
                    val plotTraces = plot.traces.map { index -> traces[index] }
                    plotTraces.fastForEachIndexed { _, trace ->
                        if (trace.isVisible) {
                            val path = trace.generatePath(plot, size, plottingMode, 75)
                            drawPath(path, trace.color, style = Stroke(2.dp.toPx()))
                            view.pointsDrawn += trace.size
                        }
                    }
                }

            }
        ) {
            drawPlot(plot, size, textMeasure, style)
        }
        if (view.drawMarkers) {
            view.markers.fastForEach { marker ->
                Canvas(modifier = Modifier
                    .drawWithContent {
                        view.redrawTrigger++
                        view.setMarkerOffset(marker.id, plot.toOffset(marker.index.toFloat(), marker.value, size, style.padding.toInt()))
                        if (marker.id == view.selectedMarkerID) {
                            val y = marker.offset.y - 50f - 15f
                            drawRect(selected, Offset(marker.offset.x, y), Size(50f, 10f))
                        }
                        drawMarker(marker.offset, view.traceColors[marker.trace], 50f, 50f)
                    }
                    .graphicsLayer {
//                        updateTransition(view.redrawTrigger)
                        shadowElevation = 0.5f
                    }
                ) {
                    //drawMarker(marker.offset, view.traceColors[marker.trace], 50f, 50f)
                }
            }
        }
    }
}



//if (view.drawMarkers) {
//    view.markers.fastForEach { marker ->
//        val offset = plot.toOffset(marker.index.toFloat(), marker.value, size, style.padding.toInt())
//        view.setMarkerOffset(marker.id, offset)
//        drawMarker(offset, view.traceColors[marker.trace], 50f, 50f)
//    }
//}

//                DraggableMarker(marker, offset, view.traceColors[marker.trace], plot, view) { offset ->
//                    // val x = mapValue(xValue.toDouble(), 0.0, packetSize.toDouble(), 0.0 + padding, size.width.toDouble())
//                    val index = mapValue(offset.x, 0f + 75f, size.width, 0f, view.packetSize.toFloat())
//                    marker.index = index.toInt()
//                    println(index)
//                    println(offset)
//                }
@Composable
fun DraggableMarker(marker: Marker, offset: Offset, color: Color, plot: Plot, view: PlotViewModel, onDrag: (Offset)->Unit) {
    Canvas(modifier = Modifier
        //.padding(start = offset.x.dp, top = offset.y.dp)
        .size(100.dp, 100.dp)
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                change.consume()
                onDrag(dragAmount)
            }
        }
    ) {
        view.redrawTrigger++
        //drawRect(Color.Red, Offset.Zero, Size(50f, 50f))
        drawMarker(offset, color, 50f, 50f)
    }
}


//        .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
//        .size(50.dp, 50.dp)
//        .background(color)
fun DrawScope.drawPlot(plot: Plot, size: Size, textMeasure: TextMeasurer, style: PlotStyle = PlotStyle.Default, showXAxis: Boolean = true) {
    drawVerticalAxis(plot.y, size, 75f, textMeasure = textMeasure, style)
    drawHorizontalAxis(plot.x, size, 75f, textMeasure = textMeasure, style)
}

fun DrawScope.drawVerticalAxis(axis: Axis, size: Size, padding: Float = 50f, textMeasure: TextMeasurer, style: PlotStyle = PlotStyle.Default) {
    val precision = calculateRequiredPrecision(axis.max, axis.min, axis.segment)
    for (yValue in axis.range()) {//floatRange(axis.min, axis.max, axis.segment)) {
        val y = mapValue(yValue, axis.min, axis.max, size.height - padding, 0f)
        val color = if (yValue == axis.min || yValue == 0f) style.axisColor else style.gridColor
        val textLayout = textMeasure.measure(yValue.formatLabelText(precision), style.textStyle)

//        drawText(textLayout, topLeft = Offset(0f - 10f, y - 10f))
//        drawLine(style.axisColor, Offset(padding, y), Offset(padding - style.tickSize, y))
//        drawLine(color, Offset(padding, y), Offset(size.width, y))

        drawGridLine(Offset(padding, y), Offset(size.width, y), textLayout, color, style)
    }

}

fun DrawScope.drawHorizontalAxis(axis: Axis, /*packetSize: Int, pointCount: Int, */size: Size, padding: Float = 50f, textMeasure: TextMeasurer, style: PlotStyle = PlotStyle.Default) {
    //val step = packetSize / axis.ticks.tickCount
    val precision = calculateRequiredPrecision(axis.max, axis.min, axis.segment)
    for (xValue in axis.range()) {//floatRange(axis.min, axis.max, axis.segment)) {
        val x = mapValue(xValue, axis.min, axis.max, 0f + padding, size.width)
        val color = if (xValue == axis.min || xValue == 0f) style.axisColor else style.gridColor
        val textLayout = textMeasure.measure(xValue.formatLabelText(precision), style.textStyle)

//        drawText(textLayout, topLeft = Offset(x - 20f, size.height - (padding / 3) * 2))
//        drawLine(style.axisColor, Offset(x, size.height - padding + style.tickSize), Offset(x, size.height - padding))
//        drawLine(color, Offset(x, 0f), Offset(x, size.height - padding))

        drawGridLine(Offset(x, 0f), Offset(x, size.height - padding), textLayout, color, style)
    }
}

fun DrawScope.drawGridLine(start: Offset, end: Offset, textLayout: TextLayoutResult, color: Color = PlotStyle.Default.gridColor, style: PlotStyle = PlotStyle.Default) {
    val horizontalAxisLine = start.y != end.y
    if (!horizontalAxisLine || horizontalAxisLine && style.showHorizontalLabels) {

        val textX = if (horizontalAxisLine) start.x - (textLayout.size.width / 2) else start.x - style.labelPadding
        val textY = if (horizontalAxisLine) end.y + style.labelOffset else start.y - 12f
        drawText(textLayout, topLeft = Offset(textX, textY))

        val tickX = if (horizontalAxisLine) end.x else start.x - style.tickSize
        val tickY = if (horizontalAxisLine) end.y + style.tickSize else start.y
        if (horizontalAxisLine) drawLine(style.axisColor, end, Offset(tickX, tickY))
        else drawLine(style.axisColor, Offset(tickX, tickY), start)
    }
    drawLine(color, start, end)
}

fun <T: Number> generatePath(values: List<Double>, min: T, max: T, size: Size, padding: Int) : Path {
//    println("Size: $size")
    val path: Path = Path()
    for (i in values.indices) {
        val x = mapValue(i.toDouble(), 0.0, values.size.toDouble(), 0.0 + padding, size.width.toDouble())
        val y = mapValue(values[i].toDouble().let { value ->
            if (value > max.toDouble()) max.toDouble()
            else if (value < min.toDouble()) min.toDouble()
            else value
        }, min.toDouble(), max.toDouble(), size.height.toDouble() - padding, 0.0)

        if (i == 0) path.moveTo(x, y)
        else path.lineTo(x, y)
    }
    return path
}

fun toOffset(xValue: Float, yValue: Float, packetSize: Int, min: Float, max: Float, size: Size, padding: Int = 75): Offset {
    val x = mapValue(xValue.toDouble(), 0.0, packetSize.toDouble(), 0.0 + padding, size.width.toDouble())
    val y = mapValue(yValue.toDouble().let { value ->
        if (value > max.toDouble()) max.toDouble()
        else if (value < min.toDouble()) min.toDouble()
        else value
    }, min.toDouble(), max.toDouble(), size.height.toDouble() - padding, 0.0)

    return Offset(x, y)
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

fun Offset.map(xFromMin: Float, xFromMax: Float, xToMin: Float, xToMax: Float, yFromMin: Float, yFromMax: Float, yToMin: Float, yToMax: Float) = Offset(
    x = mapValue(this.x, xFromMin, xFromMax, xToMin, xToMax),
    y = mapValue(this.y, yFromMin, yFromMax, yToMin, yToMax)
)

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
                    .drawWithContent {
                        drawContent()

                        if (drawNewData) view.traces.fastForEachIndexed { index, trace ->
                            val values = if (view.plottingMode == PlottingMode.FRAMES) trace.getFramesWindow() else trace.getPlotWindow()
                            val path = generatePath(values.map { it.first }, view.plot.y.min, view.plot.y.max, size, padding = 75)
                            drawPath(path, view.traceColors[index], style = Stroke(2.dp.toPx()))
                            if (index == 0) view.pointsDrawn += view.traces[0].size
                        }
                    }

            ) {
                drawPlot(view.plot, size, textMeasure)
            }
        }
    }
}