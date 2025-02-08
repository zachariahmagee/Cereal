package zm.experiment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.LaptopMac
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import zm.experiment.model.type.SidePanelType
import zm.experiment.view.*
import zm.experiment.view.icon.IconItem
import zm.experiment.view.theme.AppTheme
import zm.experiment.view.theme.AppTheme.custom
import zm.experiment.view.window.SerialMonitorWindow
import zm.experiment.viewmodel.AppViewModel
import zm.experiment.viewmodel.PlotViewModel
import zm.experiment.viewmodel.SerialMonitorViewModel


@Composable
@Preview
fun App(plot: PlotViewModel = PlotViewModel(), serial: SerialMonitorViewModel = SerialMonitorViewModel(), view: AppViewModel = AppViewModel(plot, serial)) {

    AppTheme {
        var showContent by remember { mutableStateOf(false) }
        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)//Color.White)
        )
        Row(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
        ) {
            Sidebar(view)
            AnimatedVisibility(view.sidepanelVisible()) {
                when (view.currentPanel) {
                    SidePanelType.SETTINGS -> Settings(view) //Sidepanel(view)
                    SidePanelType.PROPERTIES -> Properties(plot, onClose = { view.hidePanel() } )
                    SidePanelType.MARKERS -> Markers(plot, onClose = { view.hidePanel() })
                    SidePanelType.HELP -> Help(view, onClose = { view.hidePanel() })
                    SidePanelType.NONE -> {} // No panel visible
                }
            }
            Plotter(plot)
        }

        if (view.showSerialMonitor) {
            SerialMonitorWindow(
                serial = serial,
                onCloseRequest = { view.serialMonitorVisibility(false) }
            )
        }
    }
}
