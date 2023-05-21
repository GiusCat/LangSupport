package org.progmob.langsupport

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.progmob.langsupport.databinding.ActivityMainBinding
import org.progmob.langsupport.model.DataViewModel

/*
* TODO:
*  - Re-implement word adding (with timestamp in WordData!!)
*  - Fix word guessing (pop-up is currently based on translator)
*  - Implement TranslatorRepository and clear SearchFragment
*  - Implement StatsFragment (it's present but without real data)
*  - User management... somewhere (sign-out, name, other things?)
*/

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: DataViewModel by viewModels()

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
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}