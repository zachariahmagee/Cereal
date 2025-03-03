package zm.experiment.model.event

import zm.experiment.model.serial.Port
import zm.experiment.model.type.AlertType
import zm.experiment.model.type.SidePanelType

sealed class AppEvent {
    data class Alert(val alertType: AlertType, val message: String = "") : AppEvent()
    data class PortConnected(val port: Port) : AppEvent()
    data class PortDisconnected(val port: Port, val alert: AlertType = AlertType.NONE) : AppEvent()
    data class PanelChanged(val panel: SidePanelType = SidePanelType.NONE) : AppEvent()
    data class CommandSent(val command: String) : AppEvent()
}

