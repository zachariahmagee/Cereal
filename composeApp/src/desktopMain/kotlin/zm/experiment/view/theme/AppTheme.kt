package zm.experiment.view.theme

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color





@Composable
fun AppTheme(
    //colors: Colors = Light,
    custom: CustomColors = CustomColors(),
    typography: Typography = MaterialTheme.typography,
    shapes: Shapes = MaterialTheme.shapes,
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    val light: Colors = Light
    val dark: Colors = Dark
    val colors = if (darkTheme) Dark else Light
    val custom = custom

    CompositionLocalProvider(LocalCustomColors provides custom) {
        MaterialTheme(colors, typography, shapes, content)
    }
}


/**
 * Contains functions to access the current theme values provided at the call site's position in
 * the hierarchy.
 */
object AppTheme {
    val custom: CustomColors
        @Composable
        @ReadOnlyComposable
        get() = LocalCustomColors.current
}

