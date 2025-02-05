package zm.experiment.model.serial.commands

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import zm.experiment.model.event.AppEvent
import zm.experiment.model.event.EventBus
import zm.experiment.viewmodel.PlotViewModel

class ReceivedCommand : Command() {
    var start: Boolean = false
    var received: Boolean = false
    val re = "!\n"
    //val r: Array<Byte> = arrayOf("!".toByte())

    init {
        start = false
        received = false
    }

    override fun parse(args: String) {
        if (!start && args.length > 1) {
            start = true
        }
        else if (start) received = true
    }

    override fun execute(plot: PlotViewModel) {
        if (received) {
            plot.receivedCommands = true
            plot.viewModelScope.launch {
                EventBus.send(AppEvent.CommandSent(re))
            }
        }
    }
}