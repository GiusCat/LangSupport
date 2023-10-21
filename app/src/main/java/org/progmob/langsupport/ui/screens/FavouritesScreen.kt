package org.progmob.langsupport.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
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
import org.progmob.langsupport.ui.dialogs.GuessDialog
import org.progmob.langsupport.ui.composables.TopSearchBar
import org.progmob.langsupport.ui.composables.WordsList

@Composable
fun FavouriteScreen(
    viewModel: DataViewModel,
    modifier: Modifier = Modifier
) {
    var search by remember { mutableStateOf("") }
    var selectedWord: WordData? by remember { mutableStateOf(null) }

    val favWordsState by viewModel.activeFavWords.observeAsState()

    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TopSearchBar(
                text = search,
                label = stringResource(id = R.string.filter_favourites),
                onValueChange = {
                    search = it
                    viewModel.getFavWordsLike(search)
                },
                modifier = Modifier.fillMaxWidth()
            )

            WordsList(
                words = favWordsState!!,
                onItemClicked = { selectedWord = it },
                onStarClicked = { viewModel.updateFavouriteWord(it) },
                onDeleteClicked = { viewModel.deleteWord(it) }
            ) {}

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
}