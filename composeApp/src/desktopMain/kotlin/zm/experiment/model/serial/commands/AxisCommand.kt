package zm.experiment.model.serial.commands

import zm.experiment.model.type.AxisType
import zm.experiment.model.type.parseAxis
import zm.experiment.viewmodel.PlotViewModel

class AxisCommand : NamedCommand() {
    private var graph: Int = -1
    private var axisChar: String = ""

    override fun parse(args: String) {


        if (":" in args) {
            val parts = args.split(":", limit = 2)
            graph = parts[0].toIntOrNull() ?: -1
            axisChar = parts[1].substring(0, 1)
        } else {
            axisChar = args.substring(0, 1)
        }

        super.parse(args.drop(args.indexOfFirst { it.isWhitespace() }))
    }
    override fun execute(plot: PlotViewModel) {
        val min = getFloatArg("m")
        val max = getFloatArg("M")
        val div = getIntArg("d")
        val minor = getFloatArg("s")
        val major = getFloatArg("S")

//        println("$min, $max, $graph, $axisChar")
        val axisType = parseAxis(axisChar)

        if (min.isFinite() && max.isFinite() && axisType != null) {
            println("graph: $graph")
            if (graph == -1) {
                plot.setAxisRange(axisType, min, max)
            } else {
                plot.setAxisRange(graph, axisType, min, max)
            }
        }
        if (major.isFinite() && minor.isFinite()) {
            //plot.plots.setAxisSegments(graph, axis, minor, major)
        } else if (div != -1 && axisType != null) {
            plot.setAxisDivisions(axisType, div)
        }
    }
}