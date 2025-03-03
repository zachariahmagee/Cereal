package zm.experiment.model.type

enum class AlertType {
    NONE, PORT_WAS_DISCONNECTED, NO_PORTS_AVAILABLE, INVALID_PORT, UNABLE_TO_CONNECT,
    TracesAreEmpty
}

data class Alert(
    val type: AlertType,
    val message: String,
    var alert: Boolean = false,
) {
    companion object {
        val None = Alert(AlertType.NONE, "", false)
    }
}