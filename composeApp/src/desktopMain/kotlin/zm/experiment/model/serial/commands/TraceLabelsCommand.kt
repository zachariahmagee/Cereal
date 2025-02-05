package zm.experiment.model.serial.commands

import zm.experiment.viewmodel.PlotViewModel

class TraceLabelsCommand : Command() {
    private var labels: Array<String> = emptyArray()

    override fun parse(args: String) {
        labels = parseArgs<String>(args)
    }

    override fun execute(plot: PlotViewModel) {
        plot.addLabels(labels)
    }
}