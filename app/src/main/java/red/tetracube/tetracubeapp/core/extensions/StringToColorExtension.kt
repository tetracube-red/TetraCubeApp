package red.tetracube.tetracubeapp.core.extensions

import androidx.compose.ui.graphics.Color

val String.color
    get() = Color(android.graphics.Color.parseColor(this))