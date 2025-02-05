package zm.experiment.model.serial.commands

abstract class NamedCommand : Command() {
    protected val namedArgs = mutableMapOf<String, String>()

    override fun parse(args: String) {
        namedArgs.clear()
        val parts = args.split(",").map { it.trim() }

        for (part in parts) {
            val split = part.split(" ")
            if (split.size == 2) {
                namedArgs[split[0]] = split[1]
            }
        }
    }

    fun getFloatArg(name: String, default: Float = Float.NaN): Float =
        namedArgs[name]?.toFloatOrNull() ?: default

    fun getIntArg(name: String, default: Int = -1): Int =
        namedArgs[name]?.toIntOrNull() ?: default
}

