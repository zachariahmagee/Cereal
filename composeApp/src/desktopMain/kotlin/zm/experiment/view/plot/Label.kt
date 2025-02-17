package zm.experiment.view.plot

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import zm.experiment.model.Trace
import zm.experiment.view.theme.AppTheme
import zm.experiment.view.theme.PlotStyle
import zm.experiment.viewmodel.PlotViewModel

@Composable
fun Labels(view: PlotViewModel, modifier: Modifier = Modifier.height(25.dp).fillMaxWidth(), style: PlotStyle = PlotStyle.Default) {
    var scroll = rememberScrollState(0)
    Box (modifier.drawWithContent {
        drawContent()
        //drawRect(Color.Black, Offset(0f, 0f), size, style = Stroke(2f))
    }){
        Row(
            modifier.horizontalScroll(scroll),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "Plot Title", color = style.textStyle.color, fontSize = style.textStyle.fontSize)
            Spacer(modifier = Modifier.width(4.dp))
            view.traces.fastForEachIndexed { index, trace ->
                val label = if (view.traceLabels.size > index) view.traceLabels[index] else trace.label
                Label(trace, label, trace.color, view)
            }
        }
        HorizontalScrollbar(
            modifier = Modifier.align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(end = 12.dp),
            adapter = rememberScrollbarAdapter(scroll)
        )
    }
}

@Composable
fun Label(trace: Trace, label: String, color: Color, view: PlotViewModel, style: PlotStyle = PlotStyle.Default) {
    //var isVisible by mutableStateOf(trace.isVisible)
    AppTheme {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                //.padding(4.dp)
                .clickable {
                    trace.isVisible = !trace.isVisible
                    view.redrawTrigger++
                }

        ) {
            TraceColorSquare(trace.color, trace.isVisible)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, color = style.textStyle.color, fontSize = style.textStyle.fontSize)
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}




@Composable
fun TraceColorSquare(color: Color, isVisible: Boolean, modifier: Modifier = Modifier.padding(4.dp)) {
    Canvas(modifier) {
        drawTraceColorSquare(color, Offset(-5f, -5f), Size(24f, 24f), isVisible)
    }
}


fun DrawScope.label(label: String, color: Color, textMeasure: TextMeasurer) {

}

fun DrawScope.drawTraceColorSquare(color: Color, topLeft: Offset, size: Size, isVisible: Boolean) {
    drawRect(color, topLeft, size)
    drawRect(Color.Black, topLeft, size, style = Stroke(2f))
    if (!isVisible) {
        println("Its not bisible")
        drawRect(Color.Black, Offset(topLeft.x + (size.width / 4), topLeft.y + (size.height / 4)), Size(size.width / 2, size.height / 2))
    }
}
