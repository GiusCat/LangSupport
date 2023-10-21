package org.progmob.langsupport.ui.theme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val md_theme_light_primary = Color(0xFF6D4EA1)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFFECDCFF)
val md_theme_light_onPrimaryContainer = Color(0xFF270057)
val md_theme_light_secondary = Color(0xFF635B70)
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFEADEF7)
val md_theme_light_onSecondaryContainer = Color(0xFF1F182A)
val md_theme_light_tertiary = Color(0xFF7F525C)
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer = Color(0xFFFFD9E0)
val md_theme_light_onTertiaryContainer = Color(0xFF32101A)
val md_theme_light_error = Color(0xFFBA1A1A)
val md_theme_light_errorContainer = Color(0xFFFFDAD6)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_onErrorContainer = Color(0xFF410002)
val md_theme_light_background = Color(0xFFFFFBFF)
val md_theme_light_onBackground = Color(0xFF1D1B1E)
val md_theme_light_surface = Color(0xFFFFFBFF)
val md_theme_light_onSurface = Color(0xFF1D1B1E)
val md_theme_light_surfaceVariant = Color(0xFFE8E0EB)
val md_theme_light_onSurfaceVariant = Color(0xFF49454E)
val md_theme_light_outline = Color(0xFF7A757F)
val md_theme_light_inverseOnSurface = Color(0xFFF5EFF4)
val md_theme_light_inverseSurface = Color(0xFF323033)
val md_theme_light_inversePrimary = Color(0xFFD5BBFF)
val md_theme_light_shadow = Color(0xFF000000)
val md_theme_light_surfaceTint = Color(0xFF6D4EA1)
val md_theme_light_outlineVariant = Color(0xFFCBC4CF)
val md_theme_light_scrim = Color(0xFF000000)

val md_theme_dark_primary = Color(0xFFD5BBFF)
val md_theme_dark_onPrimary = Color(0xFF3D1C6F)
val md_theme_dark_primaryContainer = Color(0xFF543587)
val md_theme_dark_onPrimaryContainer = Color(0xFFECDCFF)
val md_theme_dark_secondary = Color(0xFFCEC2DB)
val md_theme_dark_onSecondary = Color(0xFF342D40)
val md_theme_dark_secondaryContainer = Color(0xFF4B4357)
val md_theme_dark_onSecondaryContainer = Color(0xFFEADEF7)
val md_theme_dark_tertiary = Color(0xFFF1B7C4)
val md_theme_dark_onTertiary = Color(0xFF4A252F)
val md_theme_dark_tertiaryContainer = Color(0xFF643B45)
val md_theme_dark_onTertiaryContainer = Color(0xFFFFD9E0)
val md_theme_dark_error = Color(0xFFFFB4AB)
val md_theme_dark_errorContainer = Color(0xFF93000A)
val md_theme_dark_onError = Color(0xFF690005)
val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
val md_theme_dark_background = Color(0xFF1D1B1E)
val md_theme_dark_onBackground = Color(0xFFE6E1E6)
val md_theme_dark_surface = Color(0xFF1D1B1E)
val md_theme_dark_onSurface = Color(0xFFE6E1E6)
val md_theme_dark_surfaceVariant = Color(0xFF49454E)
val md_theme_dark_onSurfaceVariant = Color(0xFFCBC4CF)
val md_theme_dark_outline = Color(0xFF958E99)
val md_theme_dark_inverseOnSurface = Color(0xFF1D1B1E)
val md_theme_dark_inverseSurface = Color(0xFFE6E1E6)
val md_theme_dark_inversePrimary = Color(0xFF6D4EA1)
val md_theme_dark_shadow = Color(0xFF000000)
val md_theme_dark_surfaceTint = Color(0xFFD5BBFF)
val md_theme_dark_outlineVariant = Color(0xFF49454E)
val md_theme_dark_scrim = Color(0xFF000000)


val seed = Color(0xFF6D4EA1)
val CorrectWord = Color(0xFF0C8100)
val WrongWord = Color(0xFFBE0D0D)
val light_CorrectWord = Color(0xFF096E00)
val light_onCorrectWord = Color(0xFFFFFFFF)
val light_CorrectWordContainer = Color(0xFF90FB76)
val light_onCorrectWordContainer = Color(0xFF012200)
val dark_CorrectWord = Color(0xFF74DE5D)
val dark_onCorrectWord = Color(0xFF033900)
val dark_CorrectWordContainer = Color(0xFF055300)
val dark_onCorrectWordContainer = Color(0xFF90FB76)
val light_WrongWord = Color(0xFFBE0D0D)
val light_onWrongWord = Color(0xFFFFFFFF)
val light_WrongWordContainer = Color(0xFFFFDAD5)
val light_onWrongWordContainer = Color(0xFF410001)
val dark_WrongWord = Color(0xFFFFB4A9)
val dark_onWrongWord = Color(0xFF690002)
val dark_WrongWordContainer = Color(0xFF930004)
val dark_onWrongWordContainer = Color(0xFFFFDAD5)

@Immutable
data class GuessDialogPalette(
    val default: Color = Color.Unspecified,
    val onDefault: Color = Color.Unspecified,
    val defaultContainer: Color = Color.Unspecified,
    val onDefaultContainer: Color = Color.Unspecified,
    val correct: Color = Color.Unspecified,
    val onCorrect: Color = Color.Unspecified,
    val correctContainer: Color = Color.Unspecified,
    val onCorrectContainer: Color = Color.Unspecified,
    val wrong: Color = Color.Unspecified,
    val onWrong: Color = Color.Unspecified,
    val wrongContainer: Color = Color.Unspecified,
    val onWrongContainer: Color = Color.Unspecified
)

val LocalGuessDialogPalette = staticCompositionLocalOf { GuessDialogPalette() }