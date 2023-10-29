package org.progmob.langsupport.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import org.progmob.langsupport.model.DataViewModel
import org.progmob.langsupport.ui.screens.LangSupportApp
import org.progmob.langsupport.ui.theme.LangSupportTheme


class MainActivity : AppCompatActivity() {
    private val viewModel: DataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LangSupportTheme {
                LangSupportApp(viewModel = viewModel)
            }
        }

        viewModel.setTranslators()
        viewModel.setRegularUpdater()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.closeTranslators()
    }
}