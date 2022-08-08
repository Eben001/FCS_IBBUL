package com.ebenezer.gana.fcsibbul.ui.host

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.databinding.ActivityHostLoggedInBinding
import com.ebenezer.gana.fcsibbul.ui.dialogs.DialogsNavigator
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HostActivityLoggedIn : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityHostLoggedInBinding
    private lateinit var navController: NavController
    private lateinit var dialogsNavigator: DialogsNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogsNavigator = DialogsNavigator(this)

        binding = ActivityHostLoggedInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarLogin)
        binding.navView.setNavigationItemSelectedListener(this)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_logged_in) as NavHostFragment
        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_announcement,
                R.id.navigation_excos
            ),
            binding.drawerLayout
        )

        binding.toolbarLogin.setupWithNavController(navController, appBarConfiguration)
        binding.buttonNavView.setupWithNavController(navController)
        NavigationUI.setupWithNavController(
            binding.toolbarLogin,
            navController,
            appBarConfiguration
        )
    }


    fun setDrawerLockedState(state: Int) {
        binding.drawerLayout.setDrawerLockMode(state)
    }

    fun setBottomNavigationVisibility(visibility: Int) {
        // get the reference of the bottomNavigationView and set the visibility.
        binding.buttonNavView.visibility = visibility
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.announcement -> {
                if (navController.currentDestination?.id == R.id.navigation_announcement) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                } else if (navController.currentDestination?.id == R.id.navigation_excos) {
                    navController.popBackStack(R.id.navigation_excos, true)
                }
            }
            R.id.daily_guide -> navController.navigate(R.id.navigation_daily_guide)

            R.id.bible_quotes -> Toast.makeText(this, "Bible Quotes", Toast.LENGTH_SHORT).show()
            R.id.fcs_song -> navController.navigate(R.id.navigation_fcs_song)
            R.id.admin_login -> navController.navigate(R.id.nav_graph_admin)
            R.id.contact_us -> dialogsNavigator.showContactusDialog()
            R.id.donate -> dialogsNavigator.showDonateDialog()
            R.id.rate_me -> Toast.makeText(this, "Rate me", Toast.LENGTH_SHORT).show()
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }

    }


}