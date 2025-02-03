package zm.experiment.view.window

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import zm.experiment.view.theme.AppTheme
import zm.experiment.view.theme.AppTheme.custom
import zm.experiment.viewmodel.SerialMonitorViewModel


@Composable
fun SerialMonitorWindow(serial: SerialMonitorViewModel, onCloseRequest: () -> Unit) {

    val verticalScroll = rememberScrollState(0)
    val horizontalScroll = rememberScrollState(0)

    var autoScroll by remember { mutableStateOf(true) }
    val textState = remember { mutableStateOf(serial.serialOutput.toString()) }

    LaunchedEffect(serial.serialOutput) {
        textState.value = serial.serialOutput.toString()
    }
    AppTheme {
        Window(onCloseRequest = onCloseRequest, title = "Serial Monitor: ${serial.port?.name}") {
            Box(modifier = Modifier.fillMaxSize().background(custom.background)) {
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    TextField(
                        value = textState.value,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.fillMaxSize()
                    )
//                    LazyColumn(modifier = Modifier.fillMaxSize().verticalScroll(verticalScroll).horizontalScroll(horizontalScroll)) {
//                        items(textState.value.lines()) { line ->
//                            Text(text = line)
//                        }
//                    }
                    Row {
                        Button(onClick = { autoScroll = !autoScroll }) {
                            Text(if (autoScroll) "Disable Autoscroll" else "Enable Autoscroll")
                        }
                    }
                }
                VerticalScrollbar(
                    modifier = Modifier.align(Alignment.CenterEnd)
                        .fillMaxHeight(),
                    adapter = rememberScrollbarAdapter(verticalScroll)
                )
                HorizontalScrollbar(
                    modifier = Modifier.align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .padding(end = 12.dp),
                    adapter = rememberScrollbarAdapter(horizontalScroll)
                )
            }
        }
    }
}