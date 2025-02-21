package zm.experiment.view.icon

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import zm.experiment.model.type.SidePanelType
import zm.experiment.view.theme.AppTheme
import zm.experiment.view.theme.AppTheme.custom
import zm.experiment.viewmodel.AppViewModel

@Composable
fun MarkerIcon(view: AppViewModel, modifier: Modifier = Modifier.size(30.dp, 30.dp)) {
    AppTheme {
        val bg = custom.offWhite
        val stroke = custom.idleText
        val textMeasure = rememberTextMeasurer()
        val layout = textMeasure.measure("M", style = TextStyle(color = Color.White, fontSize = 11.sp))

        Box(modifier = modifier.clickable { view.togglePanel(SidePanelType.MARKERS) }) {
            Canvas(modifier = Modifier.fillMaxSize().background(bg)) {
                drawRect(stroke,  style = Stroke(1.dp.toPx(), join = StrokeJoin.Round))

                val sm = size.width / 10f
//                val path = Path().apply {
//                    moveTo(sm, sm)
//                    lineTo(size.width - sm, sm)
//                    lineTo(size.width / 2, size.height - sm)
//                    close()
//                }
//                drawPath(path = path, color = stroke)
//
                drawMarker(Offset(size.width / 2, size.height - sm), stroke, size.width, size.height)
                drawText(layout, color = Color.White, Offset(size.width / 2 - layout.size.width / 2, size.height / 2 - 20f))
            }
        }

    }
}

fun DrawScope.drawMarker(point: Offset, color: Color, width: Float = 30f, height: Float = 30f, style: DrawStyle = Fill) {
    val smw = width / 10f
    val smh = height / 10f
    val path = Path().apply {
        moveTo(point.x, point.y)
        lineTo(point.x - width / 2 + smw, point.y - height + smh * 2)
        lineTo(point.x + width / 2 - smw, point.y - height + smh * 2)
        close()
    }
    drawPath(path = path, color = color, style = style)
}