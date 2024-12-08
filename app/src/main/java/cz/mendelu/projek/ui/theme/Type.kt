package cz.mendelu.projek.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import cz.mendelu.projek.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val MPLUSRounded1C = FontFamily(
    Font(R.font.mplus_rounded1c_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.mplus_rounded1c_black, FontWeight.Black, FontStyle.Normal),
    Font(R.font.mplus_rounded1c_extra_bold, FontWeight.ExtraBold, FontStyle.Normal),
    Font(R.font.mplus_rounded1c_light, FontWeight.Light, FontStyle.Normal),
    Font(R.font.mplus_rounded1c_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.mplus_rounded1c_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.mplus_rounded1c_thin, FontWeight.Thin, FontStyle.Normal),
)