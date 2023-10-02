package com.fcsibbul.ui.host

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.fcsibbul.R
import com.fcsibbul.databinding.ActivityHostLoggedOutBinding
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject


class HostActivityLoggedOut : AppCompatActivity() {

    private lateinit var binding: ActivityHostLoggedOutBinding
    private lateinit var navController: NavController

    private val firebaseAuth by inject<FirebaseAuth>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityHostLoggedOutBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseAuth.currentUser?.let {
            val intent = Intent(this@HostActivityLoggedOut, HostActivityLoggedIn::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_logged_out) as NavHostFragment
        navController = navHostFragment.navController


    }

    override fun onNavigateUp(): Boolean {
        return super.onNavigateUp() || super.onSupportNavigateUp()
    }


}