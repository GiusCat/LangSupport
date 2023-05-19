package org.progmob.langsupport

import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainNav:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_activity)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val host: NavHostFragment = supportFragmentManager.
            findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?:return

        val navController = host.navController
        setupBottomNavMenu(navController)

        navController.addOnDestinationChangedListener{
            _, destination, _ ->

            val dest:String = try {
                resources.getResourceName(destination.id)
            } catch (e: Resources.NotFoundException){
                Integer.toString(destination.id)
            }
        }

    }

    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav.setupWithNavController(navController)

    }

}

