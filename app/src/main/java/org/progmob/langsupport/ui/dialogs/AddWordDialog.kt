package org.progmob.langsupport.ui.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.progmob.langsupport.R
import org.progmob.langsupport.model.DataViewModel
import org.progmob.langsupport.model.WordData
import org.progmob.langsupport.ui.composables.LanguageSpinner

@Composable
fun AddWordDialog(
    inputWord: String,
    viewModel: DataViewModel,
    onDismissClick: () -> Unit,
    onConfirmClick: (WordData) -> Unit,
    modifier: Modifier = Modifier
) {
    var word by remember { mutableStateOf(inputWord) }
    var translation: String by remember { mutableStateOf("") }
    var language by remember { mutableStateOf(viewModel.lastLang ?: "en") }
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
                    verticalAlignment = Alignment.Bottom,
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
                        textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                        modifier = Modifier.fillMaxWidth(0.55f)
                    )
                    LanguageSpinner(
                        preselected = language,
                        onSelectionChanged = { selected -> language = selected },
                        modifier = Modifier.wrapContentSize()
                    )
                }

                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = translation,
                        label = { Text(stringResource(id = R.string.translation)) },
                        singleLine = true,
                        onValueChange = { translation = it },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                        modifier = Modifier.fillMaxWidth(0.55f)
                    )

                    Button(
                        onClick = { viewModel.translateWord(word, language) { translation = it } },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier.wrapContentSize()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.translate_24),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onTertiary)
                        )
                    }
                }

                Spacer(
                    modifier = Modifier
                        .height(16.dp)
                        .fillMaxWidth()
                )

                TextField(
                    value = info,
                    label = { Text(stringResource(id = R.string.info)) },
                    maxLines = 2,
                    onValueChange = { input -> info = input },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                    modifier = Modifier.fillMaxWidth(0.7f)
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
                        onClick = {
                            val tr = translation.lowercase().trim()
                            onConfirmClick(WordData(word, listOf(tr), language, info))
                        },
                        enabled = (word.isNotEmpty() && translation.isNotEmpty())
                    ) {
                        Text(text = stringResource(id = R.string.confirm))
                    }
                }
            }
        }
    }
}