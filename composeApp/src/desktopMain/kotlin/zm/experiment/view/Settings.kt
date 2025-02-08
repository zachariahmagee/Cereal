package zm.experiment.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import zm.experiment.model.serial.Port
import zm.experiment.view.theme.AppTheme
import zm.experiment.viewmodel.AppViewModel

// Key: * = open panel, ^ = back to previous panel, -> = open new window
// Panel      -     Heading (content)
// -----------------------------------------------------------------------
// Main panel - Serial Port (Select*, Connect, Auto-Reconnect, Parameters*), Preferences (Screen Capture*, CSV*, Theme), About ->
// Select Serial Port - list of serial ports, connect^
// Port Parameters - Baud*, Line Ending*, Data Bits*, Stop Bits*, previous^
//      Baud Rate:
//      Line Ending: '\n', '\r'
//      Parity:
//      Data Bits:
//      Stop Bits:
// Screen Capture - Screenshot (Select Folder->, File Type*, Copy to Clipboard), Screen Record (Select Folder ->, Audio Input*, Select FFMPEG ->), Previous^
//      Image File Type: tiff, targa, jpeg, png [These options may need to change)
//      Audio Input: [[ dynamic ]]
// CSV - CSV Settings (Select Folder ->, Save As ->), Inputs (Keyboard, Add Source*), Record, Previous^
//      Add Source: Select Port*, Column Title ->, Parameters*, Remove, Previous^zzzz

sealed class SettingsMenu {
    data object MainMenu : SettingsMenu()
    data object SelectSerialPortMenu : SettingsMenu()
    data object PortParametersMenu : SettingsMenu()
    data object BaudRateMenu : SettingsMenu()
    data object LineEndingMenu : SettingsMenu()
    data object ParityMenu : SettingsMenu()
    data object DataBitsMenu : SettingsMenu()
    data object StopBitsMenu : SettingsMenu()
    data object ScreenCaptureMenu : SettingsMenu()
    data object ImageFileTypeMenu : SettingsMenu()
    data object AudioInputMenu : SettingsMenu()
    data object CSVMenu : SettingsMenu()
    data object AddSourceMenu : SettingsMenu()
}

@Composable
fun Settings(view: AppViewModel) {
    val verticalScroll = rememberScrollState()
    val modifier: Modifier = Modifier.width(150.dp)
    SidePanel("Settings", modifier = modifier, onClose = { view.hidePanel() }) {
        Column(modifier = modifier.verticalScroll(verticalScroll).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            SettingsSection("Serial Port", modifier = modifier) {
                SelectSerialPort(view, modifier)
            }
            IconSpacer(10.dp)
            SettingsSection("Properties", modifier = modifier) {

            }
        }
    } // SidePanel
}

@Composable
fun SettingsSection(heading: String, modifier: Modifier = Modifier, content: @Composable (ColumnScope.() -> Unit)) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top) {
        Heading(heading)
        content()
    }
}

@Composable
fun SelectSerialPort(view: AppViewModel, modifier: Modifier = Modifier) {
    //Column (modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top) {
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
                    colors = ButtonDefaults.outlinedButtonColors(fill, Color.Black),

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
        } // OutlinedButton - Connect | Disconnect
    //} // Column - Probably unneeded...
}

@Composable
fun Heading(heading: String, fontSize: TextUnit = 12.sp, fontWeight: FontWeight = FontWeight.SemiBold, color: Color = Color.Blue) =//, modifier: Modifier = Modifier) =
    Text(text = heading, modifier = Modifier.fillMaxWidth(), fontSize = fontSize, fontWeight = fontWeight, color = color, textAlign = TextAlign.Center)

@Composable
fun Previous(onClick: (() -> Unit), modifier: Modifier = Modifier) {
    AppTheme {
        OutlinedButton(
            onClick = { onClick() },
            modifier = modifier
                .width(120.dp)
                .height(30.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                AppTheme.custom.selected,
                AppTheme.custom.idleText
            )
        ) {
            Text(
                text = "Previous",
                fontSize = 10.sp,
                modifier = Modifier.padding(0.dp)
            )
        }
    }
}

