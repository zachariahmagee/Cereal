package zm.experiment.model.type

sealed class PlotType {
    object Cartesian : PlotType()
    object Polar : PlotType()
    object Scatter : PlotType()
}