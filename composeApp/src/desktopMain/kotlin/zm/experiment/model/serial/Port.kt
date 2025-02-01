package zm.experiment.model.serial

import com.fazecast.jSerialComm.SerialPort
import com.fazecast.jSerialComm.SerialPortMessageListener

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
    var listener : SerialPortMessageListener? = null,
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
