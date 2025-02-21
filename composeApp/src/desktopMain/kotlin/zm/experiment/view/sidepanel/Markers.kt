package zm.experiment.view.sidepanel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zm.experiment.model.Marker
import zm.experiment.viewmodel.MarkersViewModel
import zm.experiment.viewmodel.PlotViewModel

@Composable
fun Markers(plot: PlotViewModel, view: MarkersViewModel, onClose: (() -> Unit)) {
    val scroll = rememberScrollState()
    val modifier: Modifier = Modifier.width(200.dp)
    SidePanel("Markers", modifier = modifier, onClose = { onClose() } ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxWidth()) {
                plot.markers.forEach { marker ->
                    MarkerDisplay(marker)
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            MarkerControls(view)
        }
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
fun MarkerDisplay(marker: Marker) {


}

/**
 * Tabs: Marker Controls, Harmonic Series
 * Left Arrow <-- Manual --> Right Arrow
 * Buttons: Delete Marker, Add Marker
 * */
@Composable
fun MarkerControls(view: MarkersViewModel) {

    Surface(modifier = Modifier.fillMaxWidth().height(300.dp)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = {
                    view.moveLeft()
                }) {
                    Text("Left")
                }
                Button(onClick = {
                    view.togglePeakSearch()
                }) {
                    Text(if (view.peakSearch) "Peak Search" else "Manual")
                }
                Button(onClick = {
                    view.moveRight()
                }) {
                    Text("Right")
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = {
                    view.addMarker()
                }) {
                    Text("Add Marker")
                }
                Button(onClick = {
                    view.deleteMarker()
                }) {
                    Text("Delete Marker")
                }
            }
        }
    }
}

@Composable
fun LeftArrow() {

}