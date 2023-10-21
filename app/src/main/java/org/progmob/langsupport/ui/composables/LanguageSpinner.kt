package org.progmob.langsupport.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.progmob.langsupport.ui.theme.LangSupportTheme
import org.progmob.langsupport.util.LanguageManager

@Composable
fun LanguageSpinner(
    preselected: String,
    onSelectionChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selected by rememberSaveable { mutableStateOf(preselected) }
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        OutlinedButton(
            onClick = { expanded = !expanded },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            ),
            shape = MaterialTheme.shapes.medium,
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary),
            modifier = Modifier.wrapContentSize()
        ) {
            val icon = if(expanded) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown
            LanguageSpinnerItem(
                lang = selected, 
                expanded = false,
                modifier = Modifier.padding(vertical = 2.dp)
            )
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = !expanded }
        ) {
            LanguageManager.getLanguages().forEach {
                DropdownMenuItem(
                    text = { LanguageSpinnerItem(lang = it) },
                    onClick = {
                        expanded = !expanded
                        selected = it
                        onSelectionChanged(selected)
                    }
                )
            }
        }
    }
}

@Composable
fun LanguageSpinnerItem(
    lang: String,
    modifier: Modifier = Modifier,
    expanded: Boolean = true,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {
        FlagImage(lang, size = 24.dp)

        if(expanded)
            Text(
                text = LanguageManager.nameOf(lang),
                style = MaterialTheme.typography.bodyLarge)
    }
}


@Preview(showBackground = true)
@Composable
fun SpinnerPreview() {
    LangSupportTheme {
        Column {
            LanguageSpinner(
                preselected = "en",
                onSelectionChanged = { },
                modifier = Modifier.wrapContentWidth()
            )
        }
    }
}

@Preview
@Composable
fun SpinnerItemPreview() {
    LangSupportTheme {
        LanguageSpinnerItem("en")
    }
}
