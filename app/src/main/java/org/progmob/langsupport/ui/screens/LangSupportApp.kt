package org.progmob.langsupport.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import org.progmob.langsupport.model.DataViewModel

@Composable
fun LangSupportApp(
    viewModel: DataViewModel,
    modifier: Modifier = Modifier
) {
    val user by viewModel.currUser.observeAsState()

    if(user != null)
        MainScreen(viewModel, modifier)
    else
        LoginScreen(viewModel, modifier)
}





