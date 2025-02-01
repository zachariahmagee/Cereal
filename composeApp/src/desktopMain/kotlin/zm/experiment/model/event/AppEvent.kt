package zm.experiment.model.event

import zm.experiment.model.serial.Port
import zm.experiment.model.type.AlertType

sealed class AppEvent {
    data class PortConnected(val port: Port) : AppEvent()
    data class PortDisconnected(val port: Port, val alert: AlertType = AlertType.NONE) : AppEvent()
    object PanelChanged : AppEvent()
}