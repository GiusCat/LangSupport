package org.progmob.langsupport.ui.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.progmob.langsupport.R
import org.progmob.langsupport.ui.composables.CloseButton
import org.progmob.langsupport.ui.composables.LanguageSpinner
import org.progmob.langsupport.ui.composables.LanguageSpinnerItem
import org.progmob.langsupport.ui.theme.LangSupportTheme

@Composable
fun SettingsDialog(
    defaultLanguage: String,
    email: String,
    onLanguageChange: (String) -> Unit,
    onLogout: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lineColor = MaterialTheme.colorScheme.onTertiaryContainer

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.tertiaryContainer,
            shadowElevation = 8.dp,
            border = BorderStroke(6.dp, MaterialTheme.colorScheme.tertiary),
            modifier = modifier.fillMaxWidth()
        ) {
            // External column, includes title and inner column (options)
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.tertiary,
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
                            text = "Options",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontSize = 18.sp
                            ),
                            color = MaterialTheme.colorScheme.onTertiary,
                            modifier = Modifier.wrapContentSize()
                        )

                        CloseButton(
                            size = 16.dp,
                            onClick = onDismiss,
                            iconColor = MaterialTheme.colorScheme.tertiary,
                            backgroundColor = MaterialTheme.colorScheme.onTertiary
                        )
                    }
                }

                // Internal column, collects items and spacers
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 32.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.translate_to))
                        LanguageSpinner(
                            preselected = defaultLanguage,
                            onSelectionChanged = onLanguageChange
                        )
                    }

                    CustomSpacer(color = lineColor)

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp)
                    ) {
                        Text(text = stringResource(id = R.string.e_mail_address))

                        Text(text = email)
                    }

                    CustomSpacer(color = Color.Unspecified)

                    Button(
                        onClick = onLogout,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text(text = stringResource(id = R.string.log_out))
                    }
                }
            }
        }
    }
}


@Composable
fun CustomSpacer(
    color: Color,
    modifier: Modifier = Modifier
) {
    Spacer(
        modifier = modifier
            .height(24.dp)
            .fillMaxWidth(0.8f)
            .drawBehind {
                drawLine(
                    color = color,
                    start = Offset(0f, size.height / 2),
                    end = Offset(size.width, size.height / 2),
                    strokeWidth = 4f,
                    cap = StrokeCap.Round
                )
            }
    )
}


@Preview
@Composable
fun SettingsPreview() {
    LangSupportTheme {
        var show by remember { mutableStateOf(true) }
        var lang by remember { mutableStateOf("en") }
        Surface(Modifier.fillMaxSize()) {
            AnimatedVisibility(visible = show) {
                SettingsDialog(
                    defaultLanguage = lang,
                    email = "a@test.com",
                    onLanguageChange = { lang = it },
                    onLogout = { show = false },
                    onDismiss = { show = false }
                )
            }
            LanguageSpinnerItem(lang = lang)
        }
    }
}