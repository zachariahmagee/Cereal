package zm.experiment.view.icon

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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