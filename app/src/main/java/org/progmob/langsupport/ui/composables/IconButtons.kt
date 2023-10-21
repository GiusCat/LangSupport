package org.progmob.langsupport.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.progmob.langsupport.ui.theme.LangSupportTheme

@Composable
fun CloseButton(
    size: Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconColor: Color = MaterialTheme.colorScheme.surface,
    backgroundColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    Icon(
        imageVector = Icons.Default.Close,
        contentDescription = null,
        tint = iconColor,
        modifier = modifier
            .wrapContentSize()
            .size(size)
            .clickable { onClick() }
            .drawBehind {
                drawCircle(backgroundColor, this.size.maxDimension * 0.65f, center)
            }
    )
}


@Composable
fun SubmitButton(
    size: Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconColor: Color = MaterialTheme.colorScheme.surface,
    backgroundColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    Icon(
        imageVector = Icons.Default.Check,
        contentDescription = null,
        tint = iconColor,
        modifier = modifier
            .wrapContentSize()
            .size(size)
            .clickable(enabled = enabled) { onClick() }
            .drawBehind {
                drawCircle(backgroundColor, this.size.maxDimension * 0.65f, center)
            }
    )
}


@Preview
@Composable
fun ClosePreview() {
    LangSupportTheme {
        Surface(Modifier.size(
            width = 120.dp,
            height = 320.dp,
        )) {
            CloseButton(
                size = 48.dp,
                onClick = {}
            )

        }
    }
    
}