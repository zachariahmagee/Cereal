package zm.experiment.model

import androidx.compose.ui.geometry.Offset

data class Marker(
    val id: Int,
    var value: Float,
    var index: Int,
    var trace: Int,
) {
    var selected: Boolean = false
    var mouseOver: Boolean = false
    var offset: Offset = Offset.Zero
}

