package zm.experiment.model.serial.commands

import zm.experiment.viewmodel.PlotViewModel

// Command Interface
abstract class Command {
    abstract fun parse(args: String)
    abstract fun execute(plot: PlotViewModel)
    //abstract fun isExecuted(): Boolean = false

    class LamdaCommand(private val parseFn: (String) -> Unit = {}, private val executeFn: (PlotViewModel) -> Unit) : Command() {
        override fun parse(args: String) { parseFn(args) }
        override fun execute(plot: PlotViewModel) {
            executeFn(plot)
        }
    }
}



fun command(executeFn : (plot: PlotViewModel) -> Unit): Command =
     Command.LamdaCommand(executeFn = executeFn)


fun command(parseFn: (String) -> Unit, executeFn: (PlotViewModel) -> Unit) : Command =
    Command.LamdaCommand(parseFn = parseFn, executeFn = executeFn)

inline fun <reified T> parseArgs(args: String): Array<T> {
    return args.split(",")
        .map { it.trim() }
        .map { convertToType<T>(it) }
        .toTypedArray()
}

inline fun <reified T> convertToType(value: String): T {
    return when (T::class) {
        Int::class -> value.toInt() as T
        Float::class -> value.toFloat() as T
        Double::class -> value.toDouble() as T
        Boolean::class ->  value.toBooleanStrictOrNull() as T ?: (value == "true") as T
        String::class -> value as T
        else -> throw IllegalArgumentException("Unsupported type: ${T::class}")
    }
}