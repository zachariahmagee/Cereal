package zm.experiment.model.serial.listeners

import com.fazecast.jSerialComm.SerialPort
import com.fazecast.jSerialComm.SerialPortEvent
import com.fazecast.jSerialComm.SerialPortMessageListener
import kotlinx.coroutines.*
import zm.experiment.model.serial.Parser
import zm.experiment.viewmodel.SerialMonitorViewModel

class MessageListener(
    private val parser: Parser,
    private val serial: SerialMonitorViewModel,
    private val lineEnding: ByteArray = "\n".toByteArray(),
) : SerialPortMessageListener {

    // Variables to track throughput
    private var currbps: Long = 0
    private var elapsedbps: Long = 0
    private var bytesperframe: Int = 0
    private var incomingBytes: Int = 0
    private var bytesps: Int = 0

    private val messageScope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    override fun getListeningEvents(): Int {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED
    }

    override fun getMessageDelimiter(): ByteArray {
        return lineEnding
    }

    override fun delimiterIndicatesEndOfMessage(): Boolean {
        return true
    }

    override fun serialEvent(event: SerialPortEvent) {
        //synchronized(this) {
            if (currbps == 0L) currbps = System.currentTimeMillis()
            val delimitedMessage = event.receivedData
            bytesperframe += delimitedMessage.size
            incomingBytes += delimitedMessage.size

            val s = String(delimitedMessage, Charsets.UTF_8)
            elapsedbps = System.currentTimeMillis() - currbps
            if (elapsedbps >= 1000) {
                bytesps = incomingBytes
                incomingBytes = 0
                //if (debug) println("Bytes: elapsed = $elapsedbps, bytes/s = $bytesps")
                currbps = System.currentTimeMillis()
            }

            //messageScope.launch {
                //serial.addSerialData(s)
                parser.parse(s)
            //}
        //}
    }
}