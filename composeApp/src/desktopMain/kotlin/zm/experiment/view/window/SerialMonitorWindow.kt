package zm.experiment.view.window

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import zm.experiment.view.Heading
import zm.experiment.view.theme.AppTheme
import zm.experiment.view.theme.AppTheme.custom
import zm.experiment.viewmodel.SerialMonitorViewModel


@Composable
fun SerialMonitorWindow(serial: SerialMonitorViewModel, onCloseRequest: () -> Unit) {

    val verticalScroll = rememberScrollState(0)
    val horizontalScroll = rememberScrollState(0)
    val scope = rememberCoroutineScope()
    //var autoScroll by remember { mutableStateOf(true) }
    //val textState = remember { mutableStateOf(serial.serialOutput.toString()) }


    LaunchedEffect(serial.serialOutput) {
        if (serial.autoScroll) {
            verticalScroll.scrollTo(verticalScroll.maxValue)
        }
    }
//
//
//    scope.launch {
//        if (serial.autoScroll) {
//            verticalScroll.scrollBy((verticalScroll.maxValue - verticalScroll.value).toFloat())
//        }
//    }


    AppTheme {
        Window(onCloseRequest = onCloseRequest, title = "Serial Monitor: ${serial.portName}") {
            Box(modifier = Modifier.fillMaxSize().background(custom.background).padding(8.dp)) {
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    Box (modifier = Modifier.padding(4.dp)){
//                        TextField(
//                            value = serial.serialOutput,
//                            onValueChange = {
////                                scope.launch {
////                                    if (serial.autoScroll) {
////                                        verticalScroll.scrollBy((verticalScroll.maxValue - verticalScroll.value).toFloat())
////                                    }
////                                }
//                            },
//                            readOnly = true,
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .verticalScroll(verticalScroll)
//                                .horizontalScroll(horizontalScroll)
//                        ) // TextField
                        LazyColumn (modifier = Modifier
                            .verticalScroll(verticalScroll)
                            .horizontalScroll(horizontalScroll)
                            .height(400.dp)
                            .fillMaxWidth()
                        ) {
                            item {
                                Heading("Serial Output:")
                            }
                            items(serial.lines) { line ->
                                Text(text = line, color = Color.Black)
                            }
                        }
                        VerticalScrollbar(
                            modifier = Modifier.align(Alignment.CenterEnd)
                                .fillMaxHeight()
                                .clickable {
                                    serial.autoScroll = false
                                }
                            ,
                            adapter = rememberScrollbarAdapter(verticalScroll)
                        )
                        HorizontalScrollbar(
                            modifier = Modifier.align(Alignment.BottomStart)
                                .fillMaxWidth()
                                .padding(end = 12.dp),
                            adapter = rememberScrollbarAdapter(horizontalScroll)
                        )
                    }
                    Row (modifier = Modifier.height(50.dp).fillMaxWidth()){
                        Button(onClick = { serial.autoScroll = !serial.autoScroll }) {
                            Text(if (serial.autoScroll) "Disable Autoscroll" else "Enable Autoscroll")
                        }
                    } // Row
                } // Column

            } // Box
        } // Window
    } // AppTheme
}