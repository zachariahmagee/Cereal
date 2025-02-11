package zm.experiment.model.serial.commands

import zm.experiment.model.type.PlotType
import zm.experiment.viewmodel.PlotViewModel

class CartesianCommand : Command() {
    private var numberOfPlots = 1

    override fun parse(args: String) {
        numberOfPlots = args.trim().toInt()
    }

    override fun execute(plot: PlotViewModel) {
        plot.setGraphType(PlotType.Cartesian, numberOfPlots)
    }
}