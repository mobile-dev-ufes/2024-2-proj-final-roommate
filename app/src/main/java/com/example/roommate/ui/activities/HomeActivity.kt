package com.example.roommate.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.roommate.R
import com.example.roommate.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar()
    }

    private fun setToolbar() {
        val navHostFrag =
            supportFragmentManager.findFragmentById(R.id.fragment_container_home) as NavHostFragment
        val navController = navHostFrag.navController
        binding.bottomNavMenu.setupWithNavController(navController)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        binding.toolbar.inflateMenu(R.menu.toolbar_menu)

        // Show BottomNavigationView only in specific fragments
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val showBottomNav = destination.id == R.id.fragmentAds ||
                    destination.id == R.id.fragmentMyProfile ||
                    destination.id == R.id.fragmentMyGroups

            binding.bottomNavMenu.visibility = if (showBottomNav) View.VISIBLE else View.GONE
        }
    }
}