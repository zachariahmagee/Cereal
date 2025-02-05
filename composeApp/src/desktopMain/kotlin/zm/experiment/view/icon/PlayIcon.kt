package zm.experiment.view.icon

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import zm.experiment.view.theme.AppTheme
import zm.experiment.view.theme.AppTheme.custom
import androidx.compose.ui.graphics.Path
import zm.experiment.viewmodel.AppViewModel

// TODO: Get this version to work with "playing" variable instead of needing to pass in the view model
@Composable
fun PlayIcon(modifier: Modifier = Modifier.size(30.dp, 30.dp), playing: Boolean, onClick: () -> Unit) {

    val connected by remember { mutableStateOf(playing) }

    AppTheme {
        val bg = custom.offWhite
        val stroke = custom.idleText
        val playColor = custom.greenConnect
       Box(modifier = modifier.clickable { onClick() }) {
           Canvas(modifier = Modifier.fillMaxSize().background(bg)) {
               drawRect(stroke,  style = Stroke(1.dp.toPx(), join = StrokeJoin.Round))
               if (playing) {
                   val q = size.width / 4f
                   val sm = size.height / 6f
                   drawRect(stroke, Offset(sm, sm), Size(q, size.height - sm))
                   drawRect(stroke, Offset(size.width - sm - q, sm), Size(q, size.height + sm))
               } else {
                   val sm = size.width / 6f
                   val md = size.width / 2f
                   val lg = sm * 5

                   val path = Path().apply {
                       moveTo(sm, sm)
                       lineTo(sm, lg)
                       lineTo(lg, md)
                       close()
                   }
                   drawPath(path = path, color = stroke, style = Stroke(1.dp.toPx(), join = StrokeJoin.Round))

               }
           }
       }

    }
}

@Composable
fun PlayIcon(view: AppViewModel, modifier: Modifier = Modifier.size(30.dp, 30.dp)) {
    AppTheme {
        val bg = custom.offWhite
        val stroke = custom.idleText
        val playColor = custom.greenConnect
        Box(modifier = modifier.clickable { view.setupSerial() }) {
            Canvas(modifier = Modifier.fillMaxSize().background(bg)) {
                drawRect(stroke,  style = Stroke(1.dp.toPx(), join = StrokeJoin.Round))
                if (view.primaryPort.connected) {
                    val q = size.width / 4f
                    val sm = size.height / 6f
                    drawRect(stroke, Offset(sm, sm), Size(q, size.height - sm * 2))
                    drawRect(stroke, Offset(size.width - sm - q, sm), Size(q, size.height - sm * 2))
                } else {
                    val sm = size.width / 6f
                    val md = size.width / 2f
                    val lg = sm * 5

                    val path = Path().apply {
                        moveTo(sm, sm)
                        lineTo(sm, lg)
                        lineTo(lg, md)
                        close()
                    }
                    drawPath(path = path, color = stroke, style = Stroke(1.dp.toPx(), join = StrokeJoin.Round))

                }
            }
        }

    }
}