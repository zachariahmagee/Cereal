package zm.experiment.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import zm.experiment.model.event.AppEvent
import zm.experiment.model.event.EventBus
import zm.experiment.model.serial.Port

class SerialMonitorViewModel() : ViewModel() {
    private val buffer = StringBuilder()
    val serialOutput: String get() = buffer.toString()

    var serialConnected: Boolean by mutableStateOf(false)
        private set
    var portName: String by mutableStateOf("")
        private set
//    var port: Port? by mutableStateOf<Port?>(null)
//        private set

    init {
        viewModelScope.launch {
            EventBus.events.collect { event ->
                when (event) {
                    AppEvent.PanelChanged -> TODO()
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
        if (buffer.length > 500_000) { // Approx. 10,000 lines (adjust as needed)
            buffer.delete(0, buffer.indexOf("\n") + 1) // Remove oldest line
        }
        buffer.append(data)//.append("\n")
    }
}