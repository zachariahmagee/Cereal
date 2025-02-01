package zm.experiment.view.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color


val Light = lightColors(
    primary = Color(175,238,238), // pale blue - selected
    primaryVariant = Color(96, 200, 220), // light blue - accent
    secondary = Color(0xFFF5F5F5), // fill - sidebar
    secondaryVariant = Color(0xFF07D2BE), // blueGreen
    background = Color(0xFFFFFFFF), // white
    surface = Color(250, 249, 246), // off-white
    error = Color(0xFFE65525), // orange - was Color(0xFFB00020),
    onPrimary = Color.Black,
    onSecondary = Color(0xFF162BBD), // blue - heading
    onBackground = Color(0xFFD9D9D9), // light grey - divider
    onSurface = Color(0xFF31322C), // dark grey - idle text
    onError = Color(0xFFB00020),
)

// TODO: Dark theme needs modified
val Dark = darkColors(
    primary = Color(175,238,238),
    primaryVariant = Color(96, 200, 220),
    secondary = Color(0xFF162BBD),
    secondaryVariant = Color(0x018786),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    error = Color(0xFFB00020),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White,
)

internal val LocalCustomColors = staticCompositionLocalOf { CustomColors() }
//internal val LocalLightColors = staticCompositionLocalOf { Light }
//internal val LocalDarkColors = staticCompositionLocalOf { Dark }

class CustomColors(
    background: Color = Color(0xFFFFFFFF),
    opposite: Color = Color(0xFF000000),
    divider: Color = Color(0xFFD9D9D9),
    outline: Color = Color(0xFF0A0A0A),
    sidepanel: Color = Color(0xFFE5E5E5),
    panel: Color = Color(250, 249, 246),
    fill: Color = Color(0xFFF5F5F5),

    tabs: Color = Color(250, 249, 246),
    tabText: Color = Color(0xFF323232),

    button: Color = background,
    button1: Color = background,
    button2: Color = background,
    altButton: Color = Color(250, 249, 246),

    buttonText: Color = Color(0xFF3C505A),

    idle: Color = Color(0xFFF0F0F0),
    idleText: Color = Color(0xFF31322C), // darkgrey

    selected: Color = Color(175, 238, 238),
    selectedText: Color = Color(36, 180, 179),

    heading: Color = Color(0xFF162BBD),

    highlight: Color = Color(96, 200, 220),
    accent: Color = Color(96, 200, 220),
    altAccent: Color = Color(7, 210, 190),

    error: Color = Color(230, 85, 37),
    errorText: Color = Color(212, 125, 2),
) {

    var background by mutableStateOf(background, structuralEqualityPolicy())
        internal set
    var opposite by mutableStateOf(opposite, structuralEqualityPolicy())
        internal set
    var divider by mutableStateOf(divider, structuralEqualityPolicy())
        internal set
    var outline by mutableStateOf(outline, structuralEqualityPolicy())
        internal set
    var sidepanel by mutableStateOf(sidepanel, structuralEqualityPolicy())
        internal set
    var panel by mutableStateOf(panel, structuralEqualityPolicy())
        internal set
    var fill by mutableStateOf(fill, structuralEqualityPolicy())
        internal set
    var tabs by mutableStateOf(tabs, structuralEqualityPolicy())
        internal set
    var tabText by mutableStateOf(tabText, structuralEqualityPolicy())
        internal set
    var button by mutableStateOf(button, structuralEqualityPolicy())
        internal set
    var button1 by mutableStateOf(button1, structuralEqualityPolicy())
        internal set
    var button2 by mutableStateOf(button2, structuralEqualityPolicy())
        internal set
    var altButton by mutableStateOf(altButton, structuralEqualityPolicy())
        internal set
    var buttonText by mutableStateOf(buttonText, structuralEqualityPolicy())
        internal set
    var idle by mutableStateOf(idle, structuralEqualityPolicy())
        internal set
    var idleText by mutableStateOf(idleText, structuralEqualityPolicy())
        internal set
    var selected by mutableStateOf(selected, structuralEqualityPolicy())
        internal set
    var selectedText by mutableStateOf(selectedText, structuralEqualityPolicy())
        internal set
    var heading by mutableStateOf(heading, structuralEqualityPolicy())
        internal set
    var highlight by mutableStateOf(highlight, structuralEqualityPolicy())
        internal set
    var accent by mutableStateOf(accent, structuralEqualityPolicy())
        internal set
    var altAccent by mutableStateOf(altAccent, structuralEqualityPolicy())
        internal set
    var error by mutableStateOf(error, structuralEqualityPolicy())
        internal set
    var errorText by mutableStateOf(errorText, structuralEqualityPolicy())
        internal set

    val white: Color = Color(255, 255, 255)
    val offWhite: Color = Color(250, 249, 246)
    val paleBlue: Color = Color(175, 238, 238)
    val darkPaleBlue: Color = Color(36, 180, 179)
    val lightBlue: Color = Color(96, 200, 220)
    val blue: Color = Color(40, 80, 255)
    val purple: Color = Color(147, 111, 212)
    val lavender: Color = Color(188, 117, 255)
    val red: Color = Color(208, 38, 98)
    val yellow: Color = Color(215, 196, 96)
    val green: Color = Color(35, 205, 65)
    val orange: Color = Color(230, 85, 37)
    val anotherOrange: Color = Color(212, 125, 2)
    val darkAqua: Color = Color(10, 10, 111)

    val black: Color = Color(0, 0, 0)
    val greenConnect: Color = Color(34, 139, 34)
    val pink: Color = Color(255, 192, 203)
    val darkRed: Color = Color(139, 0, 0)
    val cream: Color = Color(224, 210, 191)
    val blueGreen: Color = Color(7, 210, 190)
    val violet: Color = Color(207, 159, 255)

    val grey10: Color = Color(0xFFf5f5f6) // Dark grey (10)
    val grey25: Color = Color(0xFFe6e6e7) // Dark grey (25)
    val grey30: Color = Color(0xFFdcdcdd) // Dark grey (35)
    val grey40: Color = Color(0xFFd7d7d8) // Dark grey (40)
    val grey49: Color = Color(0xFFcececf) // Dark grey (49)
    val grey60: Color = Color(0xFFc1c1c2) // Dark grey (62)
    val grey80: Color = Color(0xFFafafb0) // Dark grey (80)
    val grey95: Color = Color(0xFFa0a0a1) // Dark grey (95)
    val grey111: Color = Color( 0xFF909091) // Grey (111)
    val grey125: Color = Color( 0xFF828283) // Grey (125)
    val grey140: Color = Color( 0xFF737374) // Grey (140)
    val grey160: Color = Color( 0xFF5f5f60) // Grey (160)
    val grey190: Color = Color( 0xFF414142) // Grey (190)
    val grey211: Color = Color( 0xFF2c2c2d) // Light grey (211)
    val grey223: Color = Color( 0xFF202021) // Light grey (223)
    val grey229: Color = Color( 0xFF1a1a1b) // Light grey (229)
    val grey245: Color = Color( 0xFFFFf5f5f5)

    val light = Light
    val dark = Dark
}

internal fun CustomColors.updateColorsFrom(other: CustomColors) {
    background = other.background
    opposite = other.opposite
    divider = other.divider
    outline = other.outline
    sidepanel = other.sidepanel
    panel = other.panel
    fill = other.fill

    tabs = other.tabs
    tabText = other.tabText

    button = other.button
    button1 = other.button1
    button2 = other.button2
    altButton = other.altButton

    buttonText = other.buttonText

    idle = other.idle
    idleText = other.idleText

    selected = other.selected
    selectedText = other.selectedText

    heading = other.heading

    highlight = other.highlight
    accent = other.accent
    altAccent = other.altAccent

    error = other.error
    errorText = other.errorText
}