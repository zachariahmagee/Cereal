package zm.experiment.view.sidepanel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import zm.experiment.view.theme.AppTheme
import zm.experiment.view.theme.AppTheme.custom

@Composable
fun SidePanel(
    heading: String,
    modifier: Modifier = Modifier,
//    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
//    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    onClose: () -> Unit,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: @Composable (ColumnScope.() -> Unit),
) {

    AppTheme {
        val divider = custom.divider
        val strokeWidth = 1f
        val fill = custom.panel
        Column(
            modifier = modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(fill)//Color(250, 249, 246))
                .drawWithContent {
                    drawContent()
                    drawRect(
                        color = fill,
                        Offset(0f, size.height - 40f),
                        Size(size.width, 40f)
                    )
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
            horizontalAlignment = horizontalAlignment,
            verticalArrangement = verticalArrangement,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .background(Color.White)
                    .padding(0.dp)
                    .drawWithContent {
                        drawContent()
                        drawLine(
                            color = divider,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = strokeWidth
                        )
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ) {
                Text(text = heading, fontSize = 13.sp, color = Color.Black)
                Spacer(modifier = Modifier.width(20.dp))
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onClose() }
                )
            } // Row
            Spacer(modifier = Modifier.height(5.dp))
            content()
        } // Column
    } // AppTheme
}