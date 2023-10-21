package org.progmob.langsupport.ui.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.progmob.langsupport.model.WordData
import org.progmob.langsupport.ui.theme.LangSupportTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WordsList(
    words: List<WordData>,
    onItemClicked: (WordData) -> Unit,
    onStarClicked: (WordData) -> Unit,
    onDeleteClicked: (WordData) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 8.dp),
        modifier = modifier
    ) {
        items(words, key = { word -> word.word }) { wordData ->
            WordsListItem(
                word = wordData,
                onItemClicked = { onItemClicked(wordData) },
                onStarClicked = { onStarClicked(wordData) },
                onDeleteClicked = { onDeleteClicked(wordData) },
                modifier = Modifier.animateItemPlacement()
            )
        }

        item { content() }
    }
}


@Preview
@Composable
fun ListPreview() {
    val list = List(12) {
        i -> WordData("Word $i", listOf("Translation $i"))
    }

    LangSupportTheme {
        Surface(Modifier.fillMaxSize()) {
            WordsList(words = list, {}, {}, {}, content = {
                Text("Text")
            })
        }
    }
}