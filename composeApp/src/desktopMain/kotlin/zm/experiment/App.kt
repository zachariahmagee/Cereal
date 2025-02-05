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
import zm.experiment.model.type.SidePanelType
import zm.experiment.view.Plotter
import zm.experiment.view.Sidebar
import zm.experiment.view.icon.IconItem
import zm.experiment.view.theme.AppTheme
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
                    SidePanelType.SETTINGS -> Sidepanel(view)
                    SidePanelType.PROPERTIES -> {}//SidePanel("Properties", onClose = { viewModel.hidePanel() })
                    SidePanelType.MARKERS -> {}//SidePanel("Markers", onClose = { viewModel.hidePanel() })
                    SidePanelType.HELP -> {}//SidePanel("Help", onClose = { viewModel.hidePanel() })
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

@Composable
@Preview
fun Sidepanel(view: AppViewModel) {
    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(150.dp)
                .background(Color(250, 249, 246))
                //.padding(8.dp)
                .border(1.dp, Color(217, 217, 217)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .background(Color.White)
                    .padding(0.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ) {
                Text(text = "Settings", fontSize = 13.sp, color = Color.Black)
                Spacer(modifier = Modifier.width(20.dp))
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { view.hidePanel() }
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "Serial Port", fontSize = 12.sp, color = Color.Blue)
            view.availablePorts.forEachIndexed { index, value ->
                if (!value.startsWith("tty")) {
                    val current = value == view.primaryPort.name
                    var name = view.primaryPort.name
                    if (name.contains("cu.")) name = name.split(".")[1]
                    val fill = if (current) Color(175, 238, 238) else Color.White

                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedButton(
                        onClick = {
                            view.selectPort(value, index)
                        },
                        modifier = Modifier
                            .width(120.dp)
                            .height(30.dp)
//                        .background(Color.White)
//                        .border(1.dp, Color.Black)
                        //.padding(15.dp, 5.dp),
//                    colors =
                        ,
                        colors = ButtonDefaults.outlinedButtonColors(fill, Color.Black)
                    ) {
                        Text(
                            text = value, fontSize = 10.sp, modifier = Modifier
                                .padding(0.dp)
                            //.size(10.dp)
                        )
                    }

                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                onClick = { view.setupSerial() },
                modifier = Modifier
                    .width(120.dp)
                    .height(30.dp),
                colors = if (view.primaryPort.connected) {
                    ButtonDefaults.outlinedButtonColors(
                        if (view.primaryPort.connected) Color(34, 139, 34) else Color.White,
                        Color.White
                    )
                } else {
                    ButtonDefaults.outlinedButtonColors(
                        Color.White,
                        Color.Black,
                    )
                }
            ) {
                Text(
                    text = if (view.primaryPort.connected) "Disconnect" else "Connect",
                    fontSize = 10.sp,
                    modifier = Modifier.padding(0.dp)
                )
            }
//            for (port in SerialPort.getCommPorts()) {
//                Spacer(modifier = Modifier.height(5.dp))
//                OutlinedButton(
//                    onClick = {
//
//                    },
//                    modifier = Modifier
//                        .width(120.dp)
//                        .height(30.dp)
////                        .background(Color.White)
////                        .border(1.dp, Color.Black)
//                        //.padding(15.dp, 5.dp),
////                    colors =
//                    ,
//                    colors = ButtonDefaults.outlinedButtonColors(Color.White, Color.Black)
//                ) {
//                    Text(text = port.systemPortName, fontSize = 10.sp, modifier = Modifier
//                        .padding(0.dp)
//                        //.size(10.dp)
//                    )
//                }
//
//            }
        }
    }
}





@Composable
fun SelectSerialPort() {

}