package org.progmob.langsupport.ui.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.progmob.langsupport.R
import org.progmob.langsupport.model.WordData
import org.progmob.langsupport.ui.composables.CloseButton
import org.progmob.langsupport.ui.composables.SubmitButton
import org.progmob.langsupport.ui.theme.LangSupportTheme
import org.progmob.langsupport.ui.theme.LocalGuessDialogPalette

@Composable
fun GuessDialog(
    word: WordData,
    onDismissClick: (WordData) -> Unit,
    modifier: Modifier = Modifier
) {
    var guessed: Boolean? by remember { mutableStateOf(null) }
    var translation by remember { mutableStateOf("") }
    var info: String by remember { mutableStateOf(word.info ?: "") }
    var addMeaningValue by remember { mutableStateOf("") }

    val translationList = remember { mutableStateListOf(*word.translation.map { it }.toTypedArray()) }
    var translationEnabled by remember { mutableStateOf(true) }
    var translationsExpanded by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    val dismissAction = {
        word.apply {
            this.translation = translationList.toList()
            this.info = info.trim()
            this.searched += if(guessed != null) 1 else 0
            this.guessed += if(guessed == true) 1 else 0
        }
        onDismissClick(word)
    }

    val backgroundColor = when(guessed) {
        null -> LocalGuessDialogPalette.current.defaultContainer
        true -> LocalGuessDialogPalette.current.correctContainer
        false -> LocalGuessDialogPalette.current.wrongContainer
    }
    val primaryColor = when(guessed) {
        null -> LocalGuessDialogPalette.current.default
        true -> LocalGuessDialogPalette.current.correct
        false -> LocalGuessDialogPalette.current.wrong
    }
    val dialogTitle = when(guessed) {
        null -> stringResource(id = R.string.guess_the_word)
        true -> stringResource(id = R.string.correct)
        false -> stringResource(id = R.string.wrong)
    }

    Dialog(onDismissRequest = dismissAction) {
        Surface(
            color = backgroundColor,
            shape = MaterialTheme.shapes.extraLarge,
            border = BorderStroke(6.dp, primaryColor),
            shadowElevation = 8.dp,
            modifier = modifier.fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Surface(
                    color = primaryColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = dialogTitle,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontSize = 18.sp
                            ),
                            color = backgroundColor,
                            modifier = Modifier.wrapContentSize()
                        )

                        CloseButton(
                            size = 16.dp,
                            onClick = dismissAction,
                            iconColor = primaryColor,
                            backgroundColor = backgroundColor
                        )
                    }
                }

                OutlinedTextField(
                    value = word.word,
                    label = { Text(text = stringResource(id = R.string.word)) },
                    onValueChange = {},
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryColor,
                        unfocusedBorderColor = primaryColor,
                        focusedLabelColor = primaryColor,
                        unfocusedLabelColor = primaryColor,
                        disabledBorderColor = primaryColor,
                        disabledTextColor = primaryColor,
                        disabledLabelColor = primaryColor
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp
                    )
                )

                TextField(
                    value = translation,
                    label = { Text(text = stringResource(id = R.string.insert_translation)) },
                    singleLine = true,
                    enabled = translationEnabled,
                    onValueChange = { translation = it.lowercase().trim() },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = backgroundColor,
                        focusedContainerColor = backgroundColor,
                        unfocusedLabelColor = primaryColor,
                        focusedLabelColor = primaryColor,
                        unfocusedIndicatorColor = primaryColor,
                        focusedIndicatorColor = primaryColor,
                        disabledTextColor = primaryColor,
                        disabledLabelColor = primaryColor,
                        disabledContainerColor = backgroundColor
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        textDecoration = if(guessed == false) TextDecoration.LineThrough else TextDecoration.None
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .focusRequester(focusRequester)
                )

                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                )

                if(guessed == null) {
                    Button(
                        onClick = {
                            guessed = translation.lowercase().trim() in word.translation
                            translationsExpanded = !guessed!!
                            translationEnabled = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryColor
                        )
                    ) {
                        Text(text = stringResource(id = R.string.translate))
                    }
                } else {
                    TextField(
                        value = info,
                        onValueChange = { info = it },
                        label = { Text(stringResource(id = R.string.info)) },
                        maxLines = 2,
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = backgroundColor,
                            focusedContainerColor = backgroundColor,
                            unfocusedLabelColor = primaryColor,
                            focusedLabelColor = primaryColor,
                            unfocusedTextColor = primaryColor,
                            focusedTextColor = primaryColor,
                            unfocusedIndicatorColor = primaryColor,
                            focusedIndicatorColor = primaryColor
                        ),
                        modifier = Modifier.fillMaxWidth(0.6f)
                    )

                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                    )

                    OutlinedButton(
                        onClick = { translationsExpanded = !translationsExpanded },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = primaryColor
                        )
                    ) {
                        val text = if(!translationsExpanded)
                            stringResource(id = R.string.show_translations) + " (${word.translation.size})"
                        else
                            stringResource(id = R.string.hide_translations)
                        Text(text = text)

                        Icon(
                            imageVector =
                                if(!translationsExpanded) Icons.Default.KeyboardArrowDown
                                else Icons.Default.KeyboardArrowUp, contentDescription = null,
                            tint = primaryColor
                        )
                    }
                }

                AnimatedVisibility(visible = translationsExpanded) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        LazyColumn(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(vertical = 8.dp),
                            modifier = Modifier.requiredHeight(96.dp)
                        ) {
                            itemsIndexed(translationList) {index, tr ->
                                TranslationListItem(
                                    index = index+1,
                                    translation = tr,
                                    textColor = primaryColor,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        TextField(
                            value = addMeaningValue,
                            label = {
                                Text(
                                    text = stringResource(id = R.string.add_meaning),
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            trailingIcon = {
                                SubmitButton(
                                    size = 16.dp,
                                    onClick = {
                                        translationList.add(addMeaningValue.lowercase().trim())
                                        addMeaningValue = ""
                                    },
                                    enabled = addMeaningValue.isNotEmpty(),
                                    iconColor = backgroundColor,
                                    backgroundColor = primaryColor
                                )
                            },
                            singleLine = true,
                            onValueChange = { addMeaningValue = it },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = backgroundColor,
                                unfocusedContainerColor = backgroundColor,
                                focusedLabelColor = primaryColor,
                                unfocusedLabelColor = primaryColor,
                                unfocusedIndicatorColor = primaryColor,
                                focusedIndicatorColor = primaryColor
                            ),
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}


@Composable
fun TranslationListItem(
    index: Int,
    translation: String,
    modifier: Modifier = Modifier,
    textColor: Color = Color.Unspecified
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.wrapContentHeight()
    ) {
        Text(
            text = "${index}. ",
            color = textColor,
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 18.sp
            )
        )
        Text(
            text = translation,
            color = textColor,
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 20.sp
            )
        )
    }
}


@Preview
@Composable
fun GuessPreview() {
    val word = WordData("essen", listOf("eat", "dish"), "de", "Info info info")
    var text by remember { mutableStateOf("") }
    var show by remember { mutableStateOf(true) }
    LangSupportTheme {
        Surface(Modifier.fillMaxSize()) {
            Button(
                onClick = { show = true },
                modifier = Modifier.wrapContentSize()
            ) {
                Text(text = text)
            }

            if(show)
                GuessDialog(
                    word = word,
                    onDismissClick = { w ->
                        text = "[${w.searched}, ${w.guessed}] -> ${w.translation}"
                        show = false
                    }
                )
        }
    }
}

@Preview
@Composable
fun MiscItemPreview() {
    LangSupportTheme {
        Surface(
            Modifier.size(width = 480.dp, height = 120.dp)
        ) {
            TranslationListItem(0, "aaa")
        }
    }
}