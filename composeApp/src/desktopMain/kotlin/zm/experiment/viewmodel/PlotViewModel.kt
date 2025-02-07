package zm.experiment.viewmodel

import androidx.compose.runtime.*
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

enum class PlottingMode {
    SCROLLING,
    FRAMES,
}

class PlotViewModel(

) : ViewModel() {

    val commandProcessor = CommandProcessor()
    var count: Int by mutableStateOf(0)
        private set

    val traceColors: List<Color> = TraceColors().colors
    private val _traceLabels = mutableStateListOf<String>()
    val traceLabels get() = _traceLabels

    private val _traces = mutableStateListOf<Trace>()
    val traces get() = _traces

    private val _plots = mutableStateListOf<Plot>(Plot())
    val plots get() = _plots

    val plot get() = _plots[0]

    val singlePlot by mutableStateOf(true)


    var pointsDrawn: Int by mutableStateOf(0)
    var serialConnected: Boolean by mutableStateOf(false)
        private set

    var receivedCommands: Boolean by mutableStateOf(false)

    var plottingMode: PlottingMode by mutableStateOf(PlottingMode.SCROLLING)
        private set
    var drawNewData: Boolean by mutableStateOf(false)
        private set

    var ticks: Ticks by mutableStateOf(Ticks(0.0, 500.0, 5))

    var packetSize: Int by mutableStateOf(500)
        private set


    val MIN_DELTA = 10.0

    init {
        //refreshDrawableTraces()

        viewModelScope.launch {
            EventBus.events.collect { event ->
                when (event) {
                    AppEvent.PanelChanged -> TODO()
                    is AppEvent.CommandSent -> {}
                    is AppEvent.PortConnected -> {
                        _traces.clear()
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
        if (_traces.size <= index) _traces.add(Trace(packetSize))
        _traces[index].add(yValue, xValue)
        if (index == _traces.size - 1) {
            updatePlot(index)
        }
    }

    private fun updatePlot(index: Int) {
        count++
//        fun update() {
//
//            if (singlePlot) {
//                val min = _traces.min()
//                val max = _traces.max()
//            }
//        }
        when (plottingMode) {
            PlottingMode.SCROLLING -> {
                drawNewData = true
                ticks(_traces.min(), _traces.max(), 5)
            }
            PlottingMode.FRAMES -> {
                if (_traces[index].sizeSinceLastPacket >= packetSize - 1) {
                    drawNewData = true
                    ticks(_traces.min(), _traces.max(), 5)
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

    fun ticks(min: Double, max: Double, tickCount: Int = 5, traceIndex: Int = 0) {
        if (plot.y.autoScale) {
            ticks.calculate(min, max, tickCount)
            plot.y.min = ticks.min.toFloat()
            plot.y.max = ticks.max.toFloat()
            plot.y.segment = ticks.tickStep.toFloat()//(ticks.getTick(1) - ticks.getTick(0)).toFloat()
        }
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
        when (axis) {
            AxisType.X -> {
                _plots.forEach { plot ->
                    plot.x.min = min
                    plot.x.max = max
                    plot.x.autoScale = false
                }
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

    fun setAxisDivisions(axis: AxisType, divisions: Int) {
        when (axis) {
            AxisType.X -> _plots.forEach { it.x.divisions = divisions }
            AxisType.Y -> _plots.forEach { it.y.divisions = divisions }
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


