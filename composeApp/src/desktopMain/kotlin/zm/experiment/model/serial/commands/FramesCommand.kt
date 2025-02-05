package zm.experiment.model.serial.commands

import zm.experiment.viewmodel.PlotViewModel
import zm.experiment.viewmodel.PlottingMode

class FramesCommand : Command() {
    var elapsedMs = 0L
    var currFps = 0L
    var completed = false
    var frameCount = 0

    override fun parse(args: String) {
        try {
            if (args.isNotEmpty() && args.contains("."))
                completed = true
        } catch (e: Exception) {
            println("Frames Command: $e")
        }
    }

    override fun execute(plot: PlotViewModel) {
        if (completed) {
            frameCount++
            if (currFps == 0L) currFps = System.currentTimeMillis()
//            elapsedMs = System.currentTimeMillis() - currFps
            //if (elapsedMs == 0L)
            else elapsedMs = System.currentTimeMillis() - elapsedMs
            println(elapsedMs)
            elapsedMs = System.currentTimeMillis()
//                drawNewData = true
//                ticks(_traces.min(), _traces.max(), 5)

//            elapsedMs = 0L
            if (elapsedMs >= 1000) {
                currFps = System.currentTimeMillis()
                // TODO: Count frames
                // plot.FPS = frameCount
            }
            // TODO: Set frame end for each trace
            // plot.setFrameEnd()
            completed = false
        } else plot._setPlottingMode(PlottingMode.FRAMES)
    }
}