package zm.experiment

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

enum class SidePanelType {
    NONE, SETTINGS, PROPERTIES, MARKERS, HELP,
}

enum class AlertType {
    NONE, PORT_WAS_DISCONNECTED,
}

data class Port(
    var port: SerialPort? = null,
    var name: String = "",
    var id: Int,
    var baudRate: Int = 115200,
    var serialParity: Int = 0,
    var serialDataBits: Int = 8,
    var serialStopBits: Int = 1,
    var lineEnding: Char = '\n',
    var connected: Boolean = false,
    var autoReconnect: Boolean = false,
    var lastPortName: String = "",
) {
    fun request() : Boolean {
        if (port != null && port!!.isOpen) {
            val re = "?\n"
            port!!.writeBytes(re.toByteArray(), re.toByteArray().size)
            return true
        }
        return false
    }
}

class PlotViewModel : ViewModel() {
    var currentPanel by mutableStateOf(SidePanelType.NONE)
        private set
    var port by mutableStateOf<Port>(Port(name = "", id = 0))
        private set
    var currentAlert by mutableStateOf(AlertType.NONE)
        private set

    private val _availablePorts = mutableStateListOf<String>()
    val availablePorts: List<String> = _availablePorts

    val OperatingSystem = System.getProperty("os.name")

    init {
        startBackgroundRefresh()
    }

    fun showPanel(panel: SidePanelType) {
        currentPanel = panel
    }

    fun hidePanel() {
        currentPanel = SidePanelType.NONE
    }

    fun setAlert(alert: AlertType) {
        currentAlert = alert
    }

    fun selectPort(portName: String, id: Int) {
        port = Port(name = portName, id = id, port = SerialPort.getCommPort(_availablePorts[id]))
    }

    private suspend fun refreshPorts() {


        val ports = SerialPort.getCommPorts().map { it.systemPortName }

        withContext(Dispatchers.Main) {
            var currentPortExists = false
            var different = false
            if (ports.size != _availablePorts.size) different = true
            ports.forEachIndexed { index, value ->
                if (index < _availablePorts.size && _availablePorts[index] != value) different = true
                if (port.name == value) {
                    currentPortExists = true
                    port.id = index
                }
            }

            if (port.name.startsWith("cu")) {
                val other = "tty" + port.name.substring(2)
                if (!ports.contains(other)) currentPortExists = false
            } else if (port.name.startsWith("tty")) {
                val other = "cu" + port.name.substring(3)
                if (!ports.contains(other)) currentPortExists = false
            }

            if (port.connected && !currentPortExists) {
                setupSerial()
                setAlert(AlertType.PORT_WAS_DISCONNECTED)
            }

            if (different) {
                if (port.autoReconnect && ports.contains(port.name)) setupSerial()
                if (!port.connected && ports.contains(port.name)) port.id = ports.indexOf(port.name)
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

    fun setupSerial() {

    }
}