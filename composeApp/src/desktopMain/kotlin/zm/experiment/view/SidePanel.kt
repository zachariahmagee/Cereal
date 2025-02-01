package zm.experiment.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout

@Composable
fun SidePanel(
    heading: String,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
//    val measurePolicy = columnMeasurePolicy(verticalArrangement, horizontalAlignment)
//    Layout(
//        content = { ColumnScopeInstance.content() },
//        measurePolicy = measurePolicy,
//        modifier = modifier
//    )
}