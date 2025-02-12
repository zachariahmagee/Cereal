package zm.experiment.view.plot

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.util.fastForEach
import zm.experiment.model.Trace
import zm.experiment.viewmodel.PlotViewModel

@Composable
fun Labels(traces: List<Trace>, textMeasure: TextMeasurer, view: PlotViewModel) {
//    Canvas(modifier = Modifier.fillMaxSize()) {
        Row() {
            traces.fastForEach {

            }
        }
//    }
}

@Composable
fun Label(label: String, color: Color, textmeasure: TextMeasurer) {

//    canvas(modifer = modifier.fillmaxsize()) {
//        label(label, color, textmeasure)
//    }
}


@Composable
fun TraceColorSquare(color: Color, isVisible: (()->Boolean), modifier: Modifier = Modifier) {

}


fun DrawScope.label(label: String, color: Color, textMeasure: TextMeasurer) {

}

