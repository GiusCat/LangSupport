package org.progmob.langsupport.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.progmob.langsupport.ui.theme.LangSupportTheme

@Composable
fun TopSearchBar(
    text: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    Surface(modifier = modifier) {
        OutlinedTextField(
            value = text,
            label = { Text(text = label) },
            onValueChange = onValueChange,
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Search, contentDescription = null) 
            },
            trailingIcon = {
                CloseButton(
                    size = 16.dp,
                    onClick = {
                        onValueChange("")
                        focusManager.clearFocus()
                    },
                    iconColor = MaterialTheme.colorScheme.surfaceVariant,
                    backgroundColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier.focusRequester(focusRequester)
        )
    }
}


@Preview
@Composable
fun BarPreview() {
    LangSupportTheme {
        var text by remember { mutableStateOf("") }
        TopSearchBar(
            text = text,
            label = "Search",
            onValueChange = { input -> text = input }
        )
        
        Text(text = text)
    }
}