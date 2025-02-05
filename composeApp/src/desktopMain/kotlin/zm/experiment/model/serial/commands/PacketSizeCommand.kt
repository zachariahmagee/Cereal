package zm.experiment.model.serial.commands

import zm.experiment.viewmodel.PlotViewModel

class PacketSizeCommand : Command() {
    private var args: Array<Float> = emptyArray()
    override fun parse(args: String) {
        this.args = parseArgs<Float>(args)
    }

    override fun execute(plot: PlotViewModel) {
        val temp = plot.packetSize
        if (args.size == 1) {
            plot._setPacketSize(args[0].toInt())
        }
        if (temp != plot.packetSize) {
            // primary axis counter ??
        }
    }


}