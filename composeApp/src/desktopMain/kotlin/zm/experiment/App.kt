package zm.experiment

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


@Composable
@Preview
fun App(view: PlotViewModel = PlotViewModel()) {

    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
        )
        Row(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
        ) {
            Sidebar(view)
            when (view.currentPanel) {
                SidePanelType.SETTINGS -> Sidepanel(view)
                SidePanelType.PROPERTIES -> {}//SidePanel("Properties", onClose = { viewModel.hidePanel() })
                SidePanelType.MARKERS -> {}//SidePanel("Markers", onClose = { viewModel.hidePanel() })
                SidePanelType.HELP -> {}//SidePanel("Help", onClose = { viewModel.hidePanel() })
                SidePanelType.NONE -> {} // No panel visible
            }
        }
//        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//            Button(onClick = { showContent = !showContent }) {
//                Text("Click me!")
//            }
//            AnimatedVisibility(showContent) {
//                val greeting = remember { Greeting().greet() }
//                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//                    Image(painterResource(Res.drawable.compose_multiplatform), null)
//                    Text("Compose: $greeting")
//                }
//            }
//        }
    }
}

@Composable
@Preview
fun Chart() {
    MaterialTheme {
        Canvas(Modifier.fillMaxSize()) {

        }
    }
}

@Composable
@Preview
fun Sidepanel(view: PlotViewModel) {
    MaterialTheme {
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
                //if (!value.startsWith("cu")) {
                    val current = value == view.port.name
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

                //}
            }
            Spacer(modifier = Modifier.height(5.dp))
            OutlinedButton(
                onClick = { view.setupSerial() },
                modifier = Modifier
                    .width(120.dp)
                    .height(30.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    if (view.port.connected) Color(34, 139, 34) else Color.White,
                    Color.Black
                )
            ) {
                Text(
                    text = if (view.port.connected) "Connect" else "Disconnect",
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
fun Sidebar(view: PlotViewModel) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(40.dp)
            .background(Color(229, 229, 229)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(Color.White)
                .border(1.dp, Color(10)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            IconItem(Icons.Outlined.Settings, onClick = { view.showPanel(SidePanelType.SETTINGS) })
        }
        Spacer(modifier = Modifier.height(16.dp)) // Space between icons
//        IconItem("icon2.png")
//        Spacer(modifier = Modifier.height(16.dp))
//        IconItem("icon3.png")
    }
}

@Composable
fun IconItem(icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Icon(
        imageVector = icon,
        contentDescription = null, // Add description for accessibility
        modifier = Modifier
            .size(30.dp) // Set the icon size
            .clickable { onClick() },
        tint = Color.Black // You can change the tint to any color
    )
}

@Composable
fun SelectSerialPort() {

}