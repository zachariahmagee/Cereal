package zm.experiment.model.serial

import zm.experiment.viewmodel.PlotViewModel

class Parser(
    private val plot: PlotViewModel
) {
    private val lineEnding = "\n"
    private val buf: StringBuffer = StringBuffer();
    private val isNumber = Regex("^-?\\d+(.\\d+)?$")

    fun parse(data: String) {
        buf.append(data)

        while (true) {
            // TODO: replace this with the actual line-ending
            val lineBreak = buf.indexOf(lineEnding)
            if (lineBreak == -1) break

            val line = buf.substring(0, lineBreak)
//            println(line)

            buf.delete(0, lineBreak + 1)
            if (line.isEmpty()) break
//            println("line: $line")
            if (isCommand(line)) {
                parseCommand(line)
                continue
            }

            val parts = line.trim().split("[,\\s\t]+".toRegex())

//            print("parts:")
//            parts.forEach { print(it) }
//            println()
            var validParts = 0
            var validLabels = 0

            parts.forEachIndexed { index, part ->
                var value: Double? = null
                var label: String = ""

                var part = part

                // retrieve the label if it exists
                if (":" in part) {
                    val subParts = part.split(":").map { it.trim() }

                    //label = if (subParts.isNotEmpty()) subParts[0] else ""
                    label = subParts[0]
                    part = if (subParts.size > 1) subParts[1] else ""
                }

                // TODO: parsing to find x,y or rho,theta
                if (";" in part) {
                    val point = part.split(";").map { it.trim() }

                } else {
//                    println("isNumber: ${part.matches(isNumber)}")
                    if (part.matches(isNumber)) {
                        part.toDoubleOrNull()?.let { num ->
//                            println("$line, $part, $num")
                            plot.addData(index = index, yValue = num, label = label)
                        }
                    } else {
//                        println("Not a number: $part")
                    }
                }
            }
        }
    }

    private fun isCommand(line: String): Boolean {
        return line.firstOrNull()?.let { it.isLetterOrDigit() || it == '-' } == false
    }

    private fun parseCommand(commandString: String) {
        val commandKey = commandString.take(1)
        val command = plot.commandProcessor.retrieveCommand(commandKey)
        //if (commandString.startsWith(".")) println("parseCommand: $commandString, $commandKey, ${commandString.drop(1)}")
        command?.let {
            it.parse(commandString.drop(1))
            it.execute(plot)
        }
    }

    fun extract(line: String) {
        val parts = line.split("[,\\s\t]+".toRegex())
        parts.forEachIndexed { index, part ->
            when {
                part.contains(":") && !part.contains(";") -> {
                    val (label, value) = part.split(":").map { p -> p.trim() }
                    value.toDoubleOrNull()?.let { num -> plot.addData(index = index, yValue = num, label = label)}
                }
            }
        }
    }

    fun point(part: String) {

    }
}

data class Value(val value: Double, var label: String = "")
data class Point(val val1: Double, val val2: Double, var label: String = "")