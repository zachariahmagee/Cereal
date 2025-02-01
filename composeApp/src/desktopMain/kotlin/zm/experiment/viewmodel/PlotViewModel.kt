package zm.experiment.viewmodel

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import zm.experiment.model.Trace
import zm.experiment.model.event.AppEvent
import zm.experiment.model.event.EventBus

enum class PlottingMode {
    SCROLLING,
    FRAMES,
}

class PlotViewModel : ViewModel() {
    val lightBlue: Color = Color(96, 200, 220)
    val blue: Color = Color(40, 80, 255)
    val purple: Color = Color(147, 111, 212)
    val lavender: Color = Color(188, 117, 255)
    val red: Color = Color(208, 38, 98)
    val yellow: Color = Color(215, 196, 96)
    val green: Color = Color(35, 205, 65)
    val orange: Color = Color(230, 85, 37)

    val traceColors: List<Color> = listOf<Color>(blue, red, green, orange, purple, yellow, lavender, lightBlue)

    var redrawTrigger by mutableStateOf(0)
        private set

    private val _traces = mutableStateListOf<Trace>()
    val traces get() = _traces

    private val _drawableTraces = mutableStateListOf<List<Double>>()
    val drawableTraces get() = _drawableTraces

    var serialConnected: Boolean by mutableStateOf(false)
        private set

    val packetSize: Int = 500
    val MIN_DELTA = 10.0

    init {
        //refreshDrawableTraces()
        viewModelScope.launch {
            EventBus.events.collect { event ->
                when (event) {
                    AppEvent.PanelChanged -> TODO()
                    is AppEvent.PortConnected -> {
                        serialConnected = true
                        println("PlotViewModel: Port ${event.port.name} connected")


                    }
                    is AppEvent.PortDisconnected -> {
                        serialConnected = false
                        println("Traces: ${_traces.size}")
                        _traces.forEach { trace ->
                            println(trace)
                        }
                        println("PlotViewModel: Port ${event.port.name} disconnected")
                       // _traces.clear()
                        println("After traces size: ${traces.size}")
                    }
                }
            }
        }
    }
    fun addData(index: Int, yValue: Double, xValue: Double? = null, label: String = "") {
        if (_traces.size <= index) _traces.add(Trace(packetSize))
        _traces[index].add(yValue, xValue)
        //println("$label: $value")
        redrawTrigger++
    }

    private fun refreshDrawableTraces() {
       viewModelScope.launch(Dispatchers.IO) {
           while (true) {
               withContext(Dispatchers.Main) {
                   _drawableTraces.clear()
                   _drawableTraces.addAll(_traces.map { it.getWindowValue() })
               }
               delay(5L)
           }
       }
    }
}



