package org.progmob.langsupport.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.progmob.langsupport.R
import org.progmob.langsupport.databinding.ActivityMainBinding
import org.progmob.langsupport.model.DataViewModel
import org.progmob.langsupport.model.FirebaseRepository

/*
* TODO:
*  - Better UI for pop-ups
*  - Define themes for unified color schemes
*  - String resources for multi-language support (also for error messages)
*  - User management... somewhere (sign-out, name, main language, other things?)
*  - Fix UI for sign in / sign up (main language is default system language)
*/

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: DataViewModel by viewModels()
    private val fb = FirebaseRepository

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.mail_item-> {
                Toast.makeText(this,  fb.getMail(), Toast.LENGTH_LONG).show()
                true
            }
            R.id.logOut_item-> {
                fb.signOutUser()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        if(!viewModel.isUserSignedIn())
            launchLoginActivity()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        setupBottomNavMenu(navController)
        navController.addOnDestinationChangedListener{ _, _, _ -> /* ... */ }

        viewModel.currUser.observe(this) {
            if(!viewModel.isUserSignedIn())
                launchLoginActivity()
        }
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
}