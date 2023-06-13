package org.progmob.langsupport.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.progmob.langsupport.R
import org.progmob.langsupport.databinding.ActivityMainBinding
import org.progmob.langsupport.model.DataViewModel

/*
* TODO:
*  - Better UI for pop-ups
*  - Define themes for unified color schemes
*  - String resources for multi-language support (also for error messages)
*  - User management... somewhere (sign-out, name, main language, other things?)
*  - Fix UI for sign in / sign up (main language is default system language)
*  - IMPORTANT: language selection spinner
*  - IMPORTANT: WorkManager
*  - IMPORTANT(?): word management (delete, add meaning)
*/

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: DataViewModel by viewModels()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.nickName_item ->{
                //item.title = "Lorenzo"
                true
            }
            R.id.mail_item-> {

                true
            }
            R.id.logOut_item-> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(!viewModel.isUserSignedIn())
            launchLoginActivity()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        viewModel.currUser.observe(this) {
            if(!viewModel.isUserSignedIn())
                launchLoginActivity()
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        setupBottomNavMenu(navController)
        navController.addOnDestinationChangedListener{ _, _, _ -> /* ... */ }

        viewModel.setTranslators()
        viewModel.setRegularUpdater()
    }

    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav.setupWithNavController(navController)
    }

    private fun launchLoginActivity() {
        try {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } catch (e: Exception) {
            Log.e("MainActivity", "${e.message}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.closeTranslators()
    }
}