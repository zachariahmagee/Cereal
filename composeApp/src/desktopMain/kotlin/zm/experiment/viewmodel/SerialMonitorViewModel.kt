package zm.experiment.viewmodel

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import zm.experiment.model.event.AppEvent
import zm.experiment.model.event.EventBus
import zm.experiment.model.serial.Port

class SerialMonitorViewModel() : ViewModel() {
//    private val buffer by mutableStateOf(StringBuilder())
//    val serialOutput get() = buffer

    private var _buffer by mutableStateOf("")
    val serialOutput: String get() = _buffer

    private val _lines = mutableStateListOf<String>()
    val lines: List<String> get() = _lines

    var serialConnected: Boolean by mutableStateOf(false)
        private set
    var portName: String by mutableStateOf("")
        private set
    var autoScroll by mutableStateOf(true)

    var count by mutableStateOf(0)


    init {
        viewModelScope.launch {
            EventBus.events.collect { event ->
                when (event) {
                    is AppEvent.PanelChanged -> {}
                    is AppEvent.CommandSent -> {}
                    is AppEvent.PortConnected -> {
                        serialConnected = true
                        portName = event.port.name
                        //port = event.port
                        println("SerialMonitorViewModel: Port ${event.port.name} connected")

                    }
                    is AppEvent.PortDisconnected -> {
                        serialConnected = false
                        println("SerialMonitorViewModel: Port ${event.port.name} disconnected")
                    }
                }
            }
        }
    }

    fun addSerialData(data: String) {
        //usingStrings(data)
    }

    private fun usingList(data: String) {
        if (_lines.size > 10_000) {
            repeat(10) {
                _lines.removeFirst()
            }
        }
        _lines.add(data)
    }

    private fun usingStrings(data: String) {
        val c = count++.toString()
        if (_buffer.length > 500_000) { // Approx. 10,000 lines (adjust as needed)
            //print("$count:  ${_buffer.length}, ")
            repeat(10) {
                //_buffer.delete(0, _buffer.indexOf("\n") + 1)
                _buffer = _buffer.substringAfter("\n")
            } // Remove oldest lines
            //println("${_buffer.length}")
        }
        //println("--- $c:\t$data")

        //_buffer.append("$c:\t$data")//.append("\n")
        _buffer += data
    }
}