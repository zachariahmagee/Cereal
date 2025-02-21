package zm.experiment.view.sidepanel

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zm.experiment.viewmodel.MarkersViewModel
import zm.experiment.viewmodel.PlotViewModel

@Composable
fun Markers(plot: PlotViewModel, view: MarkersViewModel, onClose: (() -> Unit)) {
    val scroll = rememberScrollState()
    val modifier: Modifier = Modifier.width(200.dp)
    SidePanel("Markers", modifier = modifier, onClose = { onClose() } ) {

    }
}

/**
 * Heading: "Marker: ID"
 * Value1
 * Value2
 * Trace selector
 * Buttons: Hide, Remove
 * */
@Composable
fun MarkerDisplay() {

}

/**
 * Tabs: Marker Controls, Harmonic Series
 * Left Arrow <-- Manual --> Right Arrow
 * Buttons: Delete Marker, Add Marker
 * */
@Composable
fun MarkerControls(view: MarkersViewModel) {

    Row(modifier = Modifier.fillMaxWidth()) {
        Button(onClick = {

        }) {
            Text("Left")
        }
        Button(onClick = {

        }) {
            Text(if (view.peakSearch) "Peak Search" else "Manual")
        }
        Button(onClick = {

        }) {
            Text("Right")
        }
    }
}

@Composable
fun LeftArrow() {

}