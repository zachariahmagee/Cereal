package zm.experiment.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fazecast.jSerialComm.SerialPort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import zm.experiment.model.event.AppEvent
import zm.experiment.model.event.EventBus
import zm.experiment.model.serial.Parser
import zm.experiment.model.serial.Port
import zm.experiment.model.serial.listeners.MessageListener
import zm.experiment.model.type.AlertType
import zm.experiment.model.type.SidePanelType


class AppViewModel(
    private val plotViewModel: PlotViewModel,
    private val serialViewModel: SerialMonitorViewModel,
) : ViewModel() {
    var currentPanel by mutableStateOf(SidePanelType.NONE)
        private set
    var primaryPort by mutableStateOf<Port>(Port(name = "", id = 0))
        private set
    var serialConnected by mutableStateOf(false)
        private set
    var currentAlert by mutableStateOf(AlertType.NONE)
        private set

    var showSerialMonitor by mutableStateOf<Boolean>(false)
        private set

    private val _availablePorts = mutableStateListOf<String>()
    val availablePorts: List<String> get() = _availablePorts

    private val parser = Parser(plotViewModel)

    val OperatingSystem = System.getProperty("os.name")

    init {
        startBackgroundRefresh()
        viewModelScope.launch {
            EventBus.events.collect { event ->
                when (event) {
                    is AppEvent.PanelChanged -> {
                        when (event.panel) {
                            SidePanelType.NONE -> {}
                            SidePanelType.SETTINGS -> {}
                            SidePanelType.PROPERTIES -> {}
                            SidePanelType.MARKERS -> {}
                            SidePanelType.HELP -> {}
                        }
                    }
                    is AppEvent.PortConnected -> {
                        println("AppViewModel: Port ${event.port.name} connected")
                        serialConnected = event.port.connected
                    }
                    is AppEvent.PortDisconnected -> {
                        println("AppViewModel: Port ${event.port.name} disconnected")
                        serialConnected = event.port.connected
                    }
                    is AppEvent.CommandSent -> {
                        sendCommandToPort(event.command)
                    }
                }
            }
        }
    }

    fun showPanel(panel: SidePanelType) {
        currentPanel = panel
    }

    fun hidePanel() {
        currentPanel = SidePanelType.NONE
    }

    fun togglePanel(panel: SidePanelType) {
        if (currentPanel == panel) hidePanel()
        else showPanel(panel)
    }

    fun serialMonitorVisibility(show: Boolean) {
        showSerialMonitor = show
    }

    fun sidepanelVisible() : Boolean { return currentPanel != SidePanelType.NONE }

    fun setAlert(alert: AlertType) {
        currentAlert = alert
    }

    fun selectPort(portName: String, id: Int) {
        primaryPort = Port(name = portName, id = id, port = SerialPort.getCommPort(_availablePorts[id]))
    }

    private fun sendCommandToPort(command: String) : Boolean {
        if (primaryPort.port!!.isOpen) {
            val bytesWritten = primaryPort.port!!.writeBytes(command.toByteArray(), command.toByteArray().size)
            return bytesWritten > 0
        }
        return false
    }

    private suspend fun refreshPorts() {
        val ports = SerialPort.getCommPorts().map { it.systemPortName }

        withContext(Dispatchers.Main) {
            var currentPortExists = false
            var different = false
            if (ports.size != _availablePorts.size) different = true
            ports.forEachIndexed { index, value ->
                if (index < _availablePorts.size && _availablePorts[index] != value) different = true
                if (primaryPort.name == value) {
                    currentPortExists = true
                    primaryPort.id = index
                }
            }

            if (primaryPort.name.startsWith("cu")) {
                val other = "tty" + primaryPort.name.substring(2)
                if (!ports.contains(other)) currentPortExists = false
            } else if (primaryPort.name.startsWith("tty")) {
                val other = "cu" + primaryPort.name.substring(3)
                if (!ports.contains(other)) currentPortExists = false
            }

            if (primaryPort.connected && !currentPortExists) {
                setupSerial()
                setAlert(AlertType.PORT_WAS_DISCONNECTED)
            }

            if (different) {
                if (primaryPort.autoReconnect && ports.contains(primaryPort.name)) setupSerial()
                if (!primaryPort.connected && ports.contains(primaryPort.name)) primaryPort.id = ports.indexOf(primaryPort.name)
            }

            _availablePorts.clear()
            _availablePorts.addAll(ports)
//            println("\n\n----refresh----")
//            _availablePorts.forEach { println(it) }
        }

    }

    private fun startBackgroundRefresh() {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                refreshPorts()
                delay(1000)
            }
        }
    }

    fun setupSerial(port: Port = primaryPort) {
        //synchronized(this) {
            if (!port.connected) {
                val ports = SerialPort.getCommPorts().map { it.systemPortName }

                if (ports.isEmpty()) setAlert(AlertType.NO_PORTS_AVAILABLE)
                else if (port.id < 0 || port.id > ports.size) setAlert(AlertType.INVALID_PORT)
                //else if (auxiliary)
                else {
                    try {
                        if (port.port == null) port.port = SerialPort.getCommPort(ports[port.id])
                        port.name = ports[port.id]
                        port.port?.openPort()
                        port.port?.setComPortParameters(
                            port.baudRate,
                            port.serialDataBits,
                            port.serialStopBits,
                            port.serialParity
                        )

                        val re = "?\n"
                        port.port?.writeBytes(re.toByteArray(), re.toByteArray().size)
                        port.listener = MessageListener(parser, serialViewModel, port.lineEnding.toString().toByteArray())
                        port.port?.addDataListener(port.listener)
                        port.connected = true

                        viewModelScope.launch {
                            EventBus.send(AppEvent.PortConnected(port))
                        }
                    } catch (e: Exception) {
                        setAlert(AlertType.UNABLE_TO_CONNECT)
                    }
                }
            } else {
                port.port?.removeDataListener()
                port.port?.closePort()
                port.connected = false

                viewModelScope.launch {
                    EventBus.send(AppEvent.PortDisconnected(port))
                }
            }
        //}
    }
}

