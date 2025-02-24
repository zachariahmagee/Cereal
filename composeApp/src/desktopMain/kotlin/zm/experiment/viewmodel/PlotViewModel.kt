package zm.experiment.viewmodel

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import zm.experiment.model.*
import zm.experiment.model.event.AppEvent
import zm.experiment.model.event.EventBus
import zm.experiment.model.serial.commands.CommandProcessor
import zm.experiment.model.type.AxisType
import zm.experiment.model.type.PlotType
import zm.experiment.model.type.SidePanelType


enum class PlottingMode {
    SCROLLING,
    FRAMES,
}

class PlotViewModel() : ViewModel() {

    val commandProcessor = CommandProcessor()
    var count: Int by mutableStateOf(0)
        private set

    val traceColors: List<Color> = TraceColors().colors
    private val _traceLabels = mutableStateListOf<String>()
    val traceLabels get() = _traceLabels

    private val _traces = mutableStateListOf<Trace>()
    val traces get() = _traces

    private val _plots = mutableStateListOf<Plot>(Plot(0))
    val plots get() = _plots

    val plot get() = _plots[0]

    val singlePlot by mutableStateOf(true)

    var numberOfPlots by mutableStateOf(0)
        private set

    var drawMarkers: Boolean by mutableStateOf(false)
    private val _markers = mutableListOf<Marker>()
    val markers get() = _markers

    var selectedMarkerID by mutableStateOf(0)

    var pointsDrawn: Int by mutableStateOf(0)
    var serialConnected: Boolean by mutableStateOf(false)
        private set

    var receivedCommands: Boolean by mutableStateOf(false)

    var plottingMode: PlottingMode by mutableStateOf(PlottingMode.SCROLLING)
        private set
    var drawNewData: Boolean by mutableStateOf(false)
        private set

    var packetSize: Int by mutableStateOf(500)
        private set

    var redrawTrigger: Int by mutableStateOf(0)

    val MIN_DELTA = 10.0

    init {
        //refreshDrawableTraces()

        viewModelScope.launch {
            EventBus.events.collect { event ->
                when (event) {
                    is AppEvent.PanelChanged -> {
                        drawMarkers = (event.panel == SidePanelType.MARKERS)
                        println("drawMarkers: $drawMarkers")
                        when (event.panel) {
                            SidePanelType.NONE -> {}
                            SidePanelType.SETTINGS -> {}
                            SidePanelType.PROPERTIES -> {}
                            SidePanelType.MARKERS -> {}
                            SidePanelType.HELP -> {}
                        }
                    }
                    is AppEvent.CommandSent -> {}
                    is AppEvent.PortConnected -> {
                        _traces.clear()
                        if (_plots.size > 1) _plots.subList(1, _plots.size).clear()
                        plot.reset()
                        count = 0
                        pointsDrawn = 0
                        serialConnected = true
                        println("PlotViewModel: Port ${event.port.name} connected")


                    }

                    is AppEvent.PortDisconnected -> {
                        serialConnected = false
                        receivedCommands = false
                        //println("Traces: ${_traces.size}")
                        _traces.forEach { trace ->
                            println(trace)
                        }
                        println("PlotViewModel: Port ${event.port.name} disconnected")
                        // _traces.clear() println("After traces size: ${traces.size}")
                    }
                }
            }
        }
    }

    fun addData(index: Int, yValue: Double, xValue: Double? = null, label: String = "") {
        if (_traces.size <= index) {
            _traces.add(Trace(packetSize, color = traceColors[index]))
            if (numberOfPlots > 1) { // also there is a better way to handle this i think.
                // TODO: Figure out what to do if the plot does not exist
                if (index <= _plots.size) _plots[index].traces.add(index)
            } else {
                plot.traces.add(index)
            }
        }
        _traces[index].add(yValue, xValue)

        if (numberOfPlots > 1) {
            updatePlots(index)
        } else if (index == _traces.size - 1) {
            updatePlot(index)
        }
    }

    private fun updatePlot(index: Int) {
        if (index == _traces.size - 1) count++

        when (plottingMode) {
            PlottingMode.SCROLLING -> {
                drawNewData = true
                if (plot.y.autoScale) plot.y.calculate(_traces.min(), _traces.max(), 5)
            }

            PlottingMode.FRAMES -> {
                //println("Counts: $count, ${_traces[index].sizeSinceLastPacket}, ${packetSize - 1}")
                if (_traces[index].sizeSinceLastPacket > packetSize - 1) {
                    drawNewData = true
                    if (plot.y.autoScale) plot.y.calculate(_traces.min(), _traces.max(), 5)
                }
            }
        }
    }

    private fun updatePlots(index: Int) {
        if (index == _traces.size - 1) count++

        val test: () -> Boolean = { plottingMode == PlottingMode.SCROLLING }
        when (plottingMode) {
            PlottingMode.SCROLLING -> {
                drawNewData = true
                if (_plots[index].y.autoScale) _plots[index].y.calculate(_traces[index].min(test), _traces[index].max(test), 5)
            }

            PlottingMode.FRAMES -> {
                //println("Counts: $count, ${_traces[index].sizeSinceLastPacket}, ${packetSize - 1}")
                if (_traces[index].sizeSinceLastPacket > packetSize - 1) {
                    drawNewData = true
                    if (_plots[index].y.autoScale) _plots[index].y.calculate(_traces[index].min(test), _traces[index].max(test), 5)
                }
            }
        }
    }

    fun addLabels(labels: Array<String>) {
        for (label in labels) _traceLabels.add(label)
    }

    fun newDataDrawn() {
        drawNewData = false
        println("Toggle draw new data")
    }

    fun _setPacketSize(packetSize: Int) {
        this.packetSize = packetSize
        for (trace in _traces) trace.windowSize = packetSize
    }

    fun _setPlottingMode(mode: PlottingMode) {
        plottingMode = mode
    }

    fun selectMarker(selectedMarkerID: Int) {
        this.selectedMarkerID = selectedMarkerID
    }

    fun createMarker(traceIndex: Int = 0, peakSearch: Boolean = true) {
        if (_markers.isNotEmpty()) {
            val index = if (peakSearch) _markers.last().index else _markers.last().index + 10 % packetSize
            val peak = if (peakSearch) _traces[traceIndex].findNextPeak(index) else (index) to _traces[traceIndex][index]!!.first
        } else {
            val peak = if (peakSearch) _traces[traceIndex].findNextPeak() else 0 to _traces[traceIndex][0]!!.first
            _markers.add(Marker(0, peak.second.toFloat(), peak.first, traceIndex))
        }
    }

    fun removeMarker(markerID: Int = _markers.size - 1) {
        _markers.removeAt(markerID)
    }

    fun setMarkerOffset(id: Int, offset: Offset) {
        _markers[id].offset = offset
        redrawTrigger++
    }

    fun moveMarkerForward(markerID: Int, peakSearch: Boolean = true) {
        val index = _markers[markerID].index
        val peak = if (peakSearch) _traces[_markers[markerID].trace].findNextPeak(index) else
            index + 1 % packetSize to _traces[_markers[markerID].trace][index + 1 % packetSize]!!.first
        println(peak)
        _markers[markerID].index = peak.first
        _markers[markerID].value = peak.second.toFloat()
    }

    fun moveMarkerBackward(markerID: Int, peakSearch: Boolean = true) {
        val index = _markers[markerID].index
        val peak = if (peakSearch) _traces[_markers[markerID].trace].findNextPeak(index) else
            index + 1 % packetSize to _traces[_markers[markerID].trace][index + 1 % packetSize]!!.first

        _markers[markerID].index = peak.first
        _markers[markerID].value = peak.second.toFloat()
    }


    private fun refreshDrawableTraces() {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                withContext(Dispatchers.Main) {

                }
                delay(5L)
            }
        }
    }

    fun setAxisRange(axis: AxisType, min: Float, max: Float) {
//        _plots.forEach { plot ->
//            plot.axes[axis.ordinal].min = min
//            plot.axes[axis.ordinal].max = max
//            plot.axes[axis.ordinal].autoScale = false
//        }
        when (axis) {
            AxisType.X -> {
                //TODO("Implement User-Defined Bounds")
//                _plots.forEach { plot ->
//                    plot.x.min = min
//                    plot.x.max = max
//                    plot.x.autoScale = false
//                }
            }

            AxisType.Y -> {
                _plots.forEach { plot ->
                    plot.y.min = min
                    plot.y.max = max
                    plot.y.autoScale = false
                }
            }
        }
    }

    fun setAxisRange(plotIndex: Int, axis: AxisType, min: Float, max: Float) {
        when (axis) {
            AxisType.X -> {
                // TODO: Implement user-defined bounds
            }
            AxisType.Y -> {
                _plots[plotIndex].y.min = min
                _plots[plotIndex].y.max = max
                _plots[plotIndex].y.autoScale = false
            }
        }

    }

    fun setAxisDivisions(axis: AxisType, divisions: Int) {
        when (axis) {
            AxisType.X -> {}//_plots.forEach { it.x.divisions = divisions }
            AxisType.Y -> _plots.forEach {
                it.y.divisions = divisions
                if (divisions != -1) it.y.segment = (it.y.max - it.y.min) / divisions
            }
        }
    }

    fun setGraphType(plotType: PlotType, numberOfPlots: Int = 1) {
        this.numberOfPlots = numberOfPlots
        println("Number of Plots: $numberOfPlots")
        when (plotType) {
            PlotType.Cartesian -> {
                if (numberOfPlots > _plots.size) {
                    plot.traces.clear()
                    plot.traces.add(0)
                    for (i in _plots.size until numberOfPlots) {
                        _plots.add(Plot(i))
                        _plots[i].traces.clear()
                        _plots[i].traces.add(i)
                    }
                } else {
                    _plots.subList(numberOfPlots, _plots.size).clear()
                }
            }

            PlotType.Polar -> TODO()
            PlotType.Scatter -> TODO()
        }
    }




}

data class TraceColors(
    val lightBlue: Color = Color(96, 200, 220),
    val blue: Color = Color(40, 80, 255),
    val purple: Color = Color(147, 111, 212),
    val lavender: Color = Color(188, 117, 255),
    val red: Color = Color(208, 38, 98),
    val yellow: Color = Color(215, 196, 96),
    val green: Color = Color(35, 205, 65),
    val orange: Color = Color(230, 85, 37),
) {
    val colors: List<Color> = listOf<Color>(blue, red, green, orange, purple, yellow, lavender, lightBlue)
//    companion object {
//        val colors: List<Color> = listOf<Color>(blue, red, green, orange, purple, yellow, lavender, lightBlue)
//    }
}


