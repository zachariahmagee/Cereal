package zm.experiment.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LaptopMac
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zm.experiment.model.type.SidePanelType
import zm.experiment.view.icon.IconItem
import zm.experiment.view.icon.MarkerIcon
import zm.experiment.view.icon.PlayIcon
import zm.experiment.view.theme.AppTheme
import zm.experiment.view.theme.AppTheme.custom
import zm.experiment.viewmodel.AppViewModel

@Composable
fun Sidebar(view: AppViewModel) {
    AppTheme {
        val divider = custom.divider
        val strokeWidth = 1f
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(40.dp)
                .background(Color(229, 229, 229))
                .drawWithContent {
                    drawContent()
                    drawLine(
                        color = divider,
                        start = Offset(size.width, 0f),
                        end = Offset(size.width, size.height),
                        strokeWidth = strokeWidth
                    )
                    drawLine(
                        color = divider,
                        start = Offset(0f, size.height - 40f),
                        end = Offset(size.width, size.height - 40f),
                        strokeWidth = strokeWidth
                    )

                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .background(Color.White)
                    .border(1.dp, Color(10))
                    .drawWithContent {
                        drawContent()
                        drawLine(
                            color = divider,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = strokeWidth
                        )
                    }
                ,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                IconItem(Icons.Outlined.Settings, onClick = { view.togglePanel(SidePanelType.SETTINGS) })
            }
            IconSpacer() //Spacer(modifier = Modifier.height(16.dp)) // Space between icons
            PlayIcon(view)
            //PlayIcon(playing = view.primaryPort.connected, onClick = { view.setupSerial() })
            IconSpacer() //Spacer(modifier = Modifier.height(16.dp))
            // TODO: Properties Panel Icon
            // TODO: Screen Capture/ Record Icon
            // TODO: Screenshot Icon
//            MarkerIcon(view)// TODO: Markers Icon
            IconSpacer()
            // TODO: CSV Icon
//            IconItem(Icons.Outlined.LaptopMac, onClick = { view.serialMonitorVisibility(!view.showSerialMonitor) })
            // TODO: Help Icon
        }
    }
}

@Composable
fun IconSpacer(height: Dp = 5.dp) = Spacer(modifier = Modifier.height(height))