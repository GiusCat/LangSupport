package org.progmob.langsupport.ui.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.progmob.langsupport.R
import org.progmob.langsupport.model.WordData
import org.progmob.langsupport.ui.composables.LanguageSpinner
import org.progmob.langsupport.ui.theme.LangSupportTheme
import org.progmob.langsupport.util.LanguageManager

@Composable
fun AddWordDialog(
    inputWord: String,
    preselectedLanguage: String,
    onTranslateClick: (String, String) -> Unit,
    onDismissClick: () -> Unit,
    onConfirmClick: (WordData) -> Unit,
    modifier: Modifier = Modifier,
    suggestedTranslation: String = ""
) {
    var word by remember { mutableStateOf(inputWord) }
    var translation: String? by remember { mutableStateOf(null) }
    var language by remember { mutableStateOf(preselectedLanguage) }
    var info by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismissClick) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = MaterialTheme.shapes.extraLarge,
            border = BorderStroke(6.dp, MaterialTheme.colorScheme.primary),
            shadowElevation = 8.dp,
            modifier = modifier.fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(
                    vertical = 16.dp,
                    horizontal = 24.dp
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = word,
                        label = { Text(stringResource(id = R.string.word)) },
                        singleLine = true,
                        onValueChange = { word = it },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier.fillMaxWidth(0.55f)
                    )
                    LanguageSpinner(
                        languages = LanguageManager.getLanguages(),
                        preselected = preselectedLanguage,
                        onSelectionChanged = { selected -> language = selected },
                        modifier = Modifier.wrapContentSize()
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = translation ?: suggestedTranslation,
                        label = { Text(stringResource(id = R.string.translation)) },
                        singleLine = true,
                        onValueChange = { translation = it },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier.fillMaxWidth(0.55f)
                    )
                    Button(
                        onClick = { onTranslateClick(word, language) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier
                            .wrapContentSize()
                            .align(Alignment.CenterVertically)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.translate_24),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onTertiary)
                        )
                    }
                }

                val spacerColor = MaterialTheme.colorScheme.background
                Spacer(
                    modifier = Modifier
                        .height(16.dp)
                        .fillMaxWidth()
                        .drawBehind {
                            drawLine(
                                color = spacerColor,
                                Offset(size.width * 0.1f, size.height),
                                Offset(size.width * 0.9f, size.height),
                                strokeWidth = 8f,
                                cap = StrokeCap.Round
                            )
                        }
                )

                TextField(
                    value = info,
                    label = { Text(stringResource(id = R.string.info)) },
                    maxLines = 2,
                    onValueChange = { input -> info = input },
                    modifier = Modifier.fillMaxWidth(0.5f)
                )

                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = onDismissClick) {
                        Text(text = stringResource(id = R.string.dismiss))
                    }
                    Button(
                        onClick = { onConfirmClick(WordData(word, listOf(translation!!), language, info)) },
                        enabled = (word.isNotEmpty() && !translation.isNullOrEmpty())
                    ) {
                        Text(text = stringResource(id = R.string.confirm))
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun DialogPreview() {
    LangSupportTheme {
        Surface(Modifier.fillMaxSize()) {
            var show by remember { mutableStateOf(false) }

            AddWordDialog("Test", "de",
                onTranslateClick =  { _, _ -> },
                onDismissClick = { show = false },
                onConfirmClick = { show = false }
            )
        }
    }
}