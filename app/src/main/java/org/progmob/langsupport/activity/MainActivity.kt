package org.progmob.langsupport.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import org.progmob.langsupport.R
import org.progmob.langsupport.model.DataViewModel
import org.progmob.langsupport.ui.screens.LangSupportApp
import org.progmob.langsupport.ui.theme.LangSupportTheme


class MainActivity : AppCompatActivity() {
    private val viewModel: DataViewModel by viewModels()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.mail_item-> {
                Toast.makeText(this, viewModel.currUser.value!!.email, Toast.LENGTH_LONG).show()
                true
            }
            R.id.logOut_item-> {
                viewModel.signOutUser()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
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