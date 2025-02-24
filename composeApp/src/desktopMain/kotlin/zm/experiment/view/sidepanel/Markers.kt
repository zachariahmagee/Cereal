package zm.experiment.view.sidepanel

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import zm.experiment.model.Marker
import zm.experiment.view.theme.AppTheme
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
    AppTheme {
        Surface(modifier = Modifier.fillMaxWidth().height(300.dp).padding(4.dp)) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton1(onClick = {
                        view.moveLeft()
                    }) {
                        Text("Left", fontSize = 10.sp, modifier = Modifier.padding(0.dp))
                    }
                    Spacer(modifier = Modifier.padding(4.dp))
                    OutlinedButton1(onClick = {
                        view.togglePeakSearch()
                    }) {
                        Text(if (view.peakSearch) "Peak Search" else "Manual", fontSize = 8.sp, modifier = Modifier.padding(0.dp))
                    }
                    Spacer(modifier = Modifier.padding(4.dp))
                    OutlinedButton1(onClick = {
                        view.moveRight()
                    }) {
                        Text("Right", fontSize = 8.sp, modifier = Modifier.padding(0.dp))
                    }
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton1(onClick = {
                        view.addMarker()
                    }) {
                        Text("Add Marker", fontSize = 8.sp, modifier = Modifier.padding(0.dp))
                    }
                    Spacer(modifier = Modifier.padding(4.dp))
                    OutlinedButton1(onClick = {
                        view.deleteMarker()
                    }) {
                        Text("Delete Marker", fontSize = 8.sp, modifier = Modifier.padding(0.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun OutlinedButton1(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,//.padding(2.dp),
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = ButtonDefaults.outlinedBorder,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(
        contentColor = Color.Black,
        backgroundColor = Color.White
    ),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        border = border,
        colors = colors,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = content
    )
}

@Composable
fun LeftArrow() {

}