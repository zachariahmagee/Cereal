package zm.experiment.model.serial.commands

import zm.experiment.viewmodel.PlotViewModel
import zm.experiment.viewmodel.PlottingMode

// Command Characters
object CommandChars {
    const val RECEIVED_COMMAND = "."
    const val PACKET_SIZE = "@"
    const val CARTESIAN = "+"
    const val SCATTER = "#"
    const val POLAR = "?"
    const val FRAMES = "$"
    const val SCROLLING = "&"
    const val ADD_TRACES = "~"
    const val UNITS = ">"
    const val DISPLAY_MODES = "<"
    const val PLOT_LABELS = "^"
    const val TRACE_COLOR = "!"
    const val GRID_COLORS = "*"
    const val TRACE_LABELS = ":"
    const val LOG_SCALE = "{"
    const val CUSTOM_GRID_LINE = "|"
    const val AXIS = "="

    const val TIMESTAMP = "/"

    const val COMMA = ","
}
// TODO: Add long form commands
object CommandStrings {
    const val receivedCommand = "[received]" //
    const val packetSize = "[packet size]" //
    const val cartesian = "[cartesian]" //
    const val scatter = "[scatter]"
    const val frames = "[frames]" //
    const val scrolling = "[scrolling]" //
    const val addTraces = "[add traces]"
    const val units = "[units]" //
    const val displayModes = "[display mode]" //
    const val plotLabels = "[plot labels]"
    const val traceColor = "[trace color]" //
    const val gridColors = "[grid colors]" //
    const val traceLabels = "[trace labels]" //
    const val logScale = "[log]" //
    const val customGridLine = "[grid line]"
    const val axis = "[axis]"
    const val timestamp = "[timestamp]"
    const val comma = "[comma]"
}



// CommandProcessor
class CommandProcessor {
    private val commands: MutableMap<String, Command> = mutableMapOf(
        CommandChars.RECEIVED_COMMAND to ReceivedCommand(),
        CommandChars.FRAMES to FramesCommand(),
        CommandChars.SCROLLING to command { plot ->
            plot._setPlottingMode(PlottingMode.SCROLLING)
        },
        CommandChars.CARTESIAN to CartesianCommand(),
//        CommandChars.POLAR to PolarCommand(),
//        CommandChars.ADD_TRACES to AddTracesCommand(),
        CommandChars.PACKET_SIZE to PacketSizeCommand(),
//        CommandChars.UNITS to UnitsCommand(),
//        CommandChars.GRID_COLORS to GridColorsCommand(),
//        CommandChars.PLOT_LABELS to PlotLabelsCommand(),
//        CommandChars.LOG_SCALE to LogarithmicCommand(),
//        CommandChars.CUSTOM_GRID_LINE to CustomGridLineCommand(),
//        CommandChars.TRACE_COLOR to TraceColorCommand(),
        CommandChars.TRACE_LABELS to TraceLabelsCommand(),
        CommandChars.AXIS to AxisCommand(),
//        CommandChars.TIMESTAMP to TimestampCommand(),
//        CommandChars.DISPLAY_MODES to DisplayModeCommand(),
//        CommandChars.SCATTER to ScatterCommand()
    )

    fun retrieveCommand(commandKey: String): Command? {
        return commands[commandKey]
    }

    fun resetCommands() {
        try {
//            val framesCommand = commands[CommandChars.FRAMES] as? FramesCommand
//            framesCommand?.compress = false
//
//            val receivedCommand = commands["."] as? ReceivedCommand
//            receivedCommand?.apply {
//                start = false
//                received = false
//            }
//
//            val plotLabelsCommand = commands[CommandChars.PLOT_LABELS] as? PlotLabelsCommand
//            plotLabelsCommand?.plotTitleReceived = false

        } catch (e: Exception) {
            e.printStackTrace() // Logs the error
        }
    }
}

