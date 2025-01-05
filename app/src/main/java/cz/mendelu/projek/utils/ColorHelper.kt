package cz.mendelu.projek.utils

import androidx.compose.ui.graphics.Color

fun parseColor(hex: String): Color {
    return Color(android.graphics.Color.parseColor(hex))
}