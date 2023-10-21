package org.progmob.langsupport.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.progmob.langsupport.R
import org.progmob.langsupport.model.DataViewModel
import org.progmob.langsupport.model.WordData
import org.progmob.langsupport.ui.dialogs.AddWordDialog
import org.progmob.langsupport.ui.dialogs.GuessDialog
import org.progmob.langsupport.ui.composables.SearchBackground
import org.progmob.langsupport.ui.composables.TopSearchBar
import org.progmob.langsupport.ui.composables.WordsList

@Composable
fun SearchScreen(
    viewModel: DataViewModel,
    modifier: Modifier = Modifier
) {
    var search by remember { mutableStateOf("") }
    var selectedWord: WordData? by remember { mutableStateOf(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    val activeWordsState by viewModel.activeWords.observeAsState(initial = listOf())

    Surface(modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TopSearchBar(
                text = search,
                label = stringResource(id = R.string.search_for_words),
                onValueChange = {
                    search = it
                    viewModel.getWordsLike(search)
                },
                modifier = Modifier
                    .fillMaxWidth()
            )

            AnimatedVisibility(search.isNotEmpty()) {
                WordsList(
                    words = activeWordsState,
                    onItemClicked = { selectedWord = it },
                    onStarClicked = { viewModel.updateFavouriteWord(it) },
                    onDeleteClicked = { viewModel.deleteWord(it) }
                ) {
                    if(activeWordsState.none { word -> word.word == search })
                        Button(onClick = { showAddDialog = true }) {
                            Text(text = stringResource(id = R.string.add_word))
                        }
                }
            }

            if(search.isEmpty())
                SearchBackground()
        }

        if(showAddDialog) {
            AddWordDialog(
                inputWord = search,
                viewModel = viewModel,
                onDismissClick = { showAddDialog = false },
                onConfirmClick = {
                    viewModel.addNewWord(it)
                    showAddDialog = false
                }
            )
        }

        if(selectedWord != null)
            GuessDialog(
                word = selectedWord!!,
                onDismissClick = {
                    viewModel.updateWord(it)
                    selectedWord = null
                }
            )
    }
}