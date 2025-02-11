package zm.experiment.view.sidepanel

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zm.experiment.viewmodel.PlotViewModel

@Composable
fun Markers(view: PlotViewModel, onClose: (() -> Unit)) {
    val modifier: Modifier = Modifier.width(200.dp)
    SidePanel("Markers", onClose = { onClose() } ) {

    }
}