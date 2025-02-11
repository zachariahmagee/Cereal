package zm.experiment.view.sidepanel

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zm.experiment.viewmodel.AppViewModel

@Composable
fun Help(view: AppViewModel, onClose: (() -> Unit)) {
    val modifier: Modifier = Modifier.width(150.dp)
    SidePanel("Help", onClose = { onClose() } ) {

    }
}