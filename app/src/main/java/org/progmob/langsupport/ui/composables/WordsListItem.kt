package org.progmob.langsupport.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.progmob.langsupport.model.WordData
import org.progmob.langsupport.ui.theme.LangSupportTheme
import org.progmob.langsupport.util.LanguageManager

@Composable
fun WordsListItem(
    word: WordData,
    onItemClicked: () -> Unit,
    onStarClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFav by remember { mutableStateOf(word.favourite) }

    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable { onItemClicked() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            FlagImage(word.lang)

            Text(
                text = word.word,
                style = MaterialTheme.typography.labelLarge
                    .copy(fontSize = 24.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .paddingFromBaseline(bottom = 6.dp)
                    .padding(horizontal = 12.dp)
                    .drawBehind {
                        val strokeWidth = 4f
                        val y = size.height - strokeWidth / 2
                        val colorStops = arrayOf(
                            0.0f to Color(0xFF1AA531),
                            (word.guessed.toFloat() / word.searched.toFloat()) to Color(0xFF1AA531),
                            (word.guessed.toFloat() / word.searched.toFloat()) to Color.Red,
                            1.0f to Color.Red
                        )

                        drawLine(
                            Brush.horizontalGradient(colorStops = colorStops),
                            Offset(0f, y),
                            Offset(size.width, y),
                            strokeWidth,
                            cap = StrokeCap.Round
                        )
                    }
            )

            IconButton(
                onClick = {
                    onStarClicked()
                    isFav = !isFav
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                Icon(
                    imageVector = if(isFav) Icons.Filled.Star else Icons.TwoTone.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiary
                )
            }
            IconButton(
                onClick = onDeleteClicked,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiary
                )
            }
        }
    }
}

@Composable
fun FlagImage(
    lang: String,
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
) {
    Image(
        painterResource(id = LanguageManager.flagOf(lang)),
        contentDescription = null,
        contentScale = ContentScale.FillHeight,
        modifier = modifier
            .size(size)
            .clip(MaterialTheme.shapes.medium)
    )
}


@Preview
@Composable
fun ItemPreview() {
    val word = WordData(
        "Goodbye",
        listOf("Test"),
        "en",
        "Info info",
        16,
        9,
        false
    )
    LangSupportTheme {
        WordsListItem(word, {}, {}, {})
    }
}