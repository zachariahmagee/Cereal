package zm.experiment.view.sidepanel

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import zm.experiment.model.Marker
import zm.experiment.model.type.PlotType
import zm.experiment.view.theme.AppTheme
import zm.experiment.view.theme.AppTheme.custom
import zm.experiment.view.theme.AppTheme.customType
import zm.experiment.viewmodel.MarkersViewModel
import zm.experiment.viewmodel.PlotViewModel

@Composable
fun Markers(plot: PlotViewModel, view: MarkersViewModel, onClose: (() -> Unit)) {
    val scroll = rememberScrollState()
    val modifier: Modifier = Modifier.width(200.dp)
    SidePanel("Markers", modifier = modifier
        .drawWithContent {
            //drawRect(Color.Blue, Offset.Zero, size, style = Stroke())
            drawContent()
        }, onClose = { onClose() } ) {
//        Column(modifier = Modifier.fillMaxSize()
//            .drawWithContent {
//                drawRect(Color.Blue, Offset.Zero, size, style = Stroke())
//                drawContent()
//            }
//            , verticalArrangement = Arrangement.Bottom) {
            Column(modifier = Modifier.fillMaxWidth().weight(1f).verticalScroll(scroll), verticalArrangement = Arrangement.Top) {
                view.markers.fastForEach { marker ->
                    val marker by remember { mutableStateOf(marker) }
                    MarkerDisplay(marker, /*marker.value, marker.value2,*/ view)
                }
            }
            //Spacer(modifier = Modifier.weight(1f))
            MarkerControls(view, Modifier)//.weight(0.4f))
//        }
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
fun MarkerDisplay(marker: Marker, /*x: Float, y: Float, */view: MarkersViewModel, plotType: PlotType = PlotType.Cartesian, modifier: Modifier = Modifier) {
    AppTheme {
        Column(modifier
            .padding(4.dp)
            .drawWithContent {
                if (view.redrawTrigger++ == 100) view.redrawTrigger = 0
                drawContent()
                drawLine(Color.Black, Offset(10f, size.height), Offset(size.width - 10f, size.height))
            }
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                if (marker.harmonic) {
                    Text(if (marker.id == 0) "Fundamental" else "Harmonic: " + marker.id, style = customType.h3, color = custom.heading)
                } else {
                    Text("Marker: " + marker.id, style = customType.h3, color = custom.heading)
                }
            }
            Spacer(Modifier.height(2.dp))
            Row(Modifier.fillMaxWidth().padding(start = 4.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                Text("Y: ${marker.value}", style = customType.h4, color = custom.idleText)
                view.redrawTrigger++
            }
            Row(Modifier.fillMaxWidth().padding(start = 4.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                Text("X: ${marker.value2}", style = customType.h4, color = custom.idleText)
                view.redrawTrigger++
            }
            Spacer(Modifier.height(2.dp))
        }
    }

}

/**
 * Tabs: Marker Controls, Harmonic Series
 * Left Arrow <-- Manual --> Right Arrow
 * Buttons: Delete Marker, Add Marker
 * */
@Composable
fun MarkerControls(view: MarkersViewModel, modifier: Modifier = Modifier, ) {
    AppTheme {
        val divider = custom.divider
        Surface(modifier = modifier.fillMaxWidth().height(125.dp).padding(4.dp)
            .drawWithContent {
                drawLine(divider, Offset.Zero, Offset(size.width, 0f), strokeWidth = 2f)
                //drawRect(Color.Red, Offset.Zero, Size(size.width, 300f), style = Stroke())
                drawContent()
            }
        ) {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
                Spacer(Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton1(onClick = {
                        view.moveLeft()
                    }) {
                        Text("Left",  fontSize = 8.sp)//style = customType.b5, modifier = Modifier.padding(0.dp))
                    }
                    Spacer(modifier = Modifier.padding(4.dp))
                    OutlinedButton1(onClick = {
                        view.togglePeakSearch()
                    }) {
                        Text(if (view.peakSearch) "Peak Search" else "Manual", fontSize = 8.sp)//style = customType.b5,  modifier = Modifier.padding(0.dp))
                    }
                    Spacer(modifier = Modifier.padding(4.dp))
                    OutlinedButton1(onClick = {
                        view.moveRight()
                    }) {
                        Text("Right", fontSize = 8.sp)//style = customType.b5, modifier = Modifier.padding(0.dp))
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    OutlinedButton1(onClick = {
                        view.addMarker()
                    }) {
                        Text("Add Marker", fontSize = 8.sp)//style = customType.b5,  modifier = Modifier.padding(0.dp))
                    }
                    Spacer(modifier = Modifier.padding(4.dp))
                    OutlinedButton1(onClick = {
                        view.deleteMarker()
                    }) {
                        Text("Delete Marker", fontSize = 8.sp);//, modifier = Modifier.padding(4.dp, 0.dp))//style = customType.b5, modifier = Modifier.padding(0.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun OutlinedButton1(
    onClick: () -> Unit,
    modifier: Modifier = Modifier.height(30.dp),//.padding(2.dp),
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = ButtonDefaults.outlinedBorder,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(
        contentColor = Color.Black,
        backgroundColor = Color.White
    ),
    contentPadding: PaddingValues = PaddingValues(0.dp),//ButtonDefaults.ContentPadding,
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