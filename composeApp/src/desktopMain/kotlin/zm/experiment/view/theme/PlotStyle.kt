package zm.experiment.view.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import zm.experiment.view.theme.AppTheme.custom

class PlotStyle (
    var axisColor: Color = Color(125, 125, 125),
    var gridColor: Color = Color(190, 190, 190),
    var textColor: Color = Color(190, 190, 190),
    var majorStroke: Float = 2f,
    var minorStroke: Float = 2f,
    val textStyle: TextStyle = TextStyle(fontSize = 12.sp, color = textColor, fontWeight = FontWeight.SemiBold)
) {
    companion object {
        val Default = PlotStyle()
    }
}