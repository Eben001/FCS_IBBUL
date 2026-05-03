package com.fcsibbul.ui.host

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.fcsibbul.R
import com.fcsibbul.core.Bars.setStatusBarColor
import com.fcsibbul.core.Bars.updateNavbarColour
import com.fcsibbul.core.Bars.inDarkMode
import com.fcsibbul.data.models.HeaderImage
import com.fcsibbul.data.notification.DailyBibleVerseWorker
import com.fcsibbul.databinding.ActivityHostLoggedInBinding
import com.fcsibbul.databinding.NavHeaderBinding
import com.fcsibbul.ui.common.Accent.setAccentColour
import com.fcsibbul.ui.common.Prefs
import com.fcsibbul.ui.dialogs.DialogsNavigator
import com.google.android.gms.ads.MobileAds
import com.google.android.material.navigation.NavigationView
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.scope.Scope
import timber.log.Timber


class HostActivityLoggedIn : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    AndroidScopeComponent {
    private lateinit var headerImageAdapter: HeaderImageAdapter
    private var autoScrollJob: Job? = null
    private var isAutoScrollPaused = false
    private val autoScrollCoroutineScope = CoroutineScope(Dispatchers.Main)

    private lateinit var binding: ActivityHostLoggedInBinding
    private lateinit var navHeaderBinding: NavHeaderBinding
    private lateinit var navController: NavController
    private val viewModel: HostActivityLoggedInViewModel by viewModel()

    override val scope: Scope by activityScope()
    private val dialogsNavigator by inject<DialogsNavigator>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) {}

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkNotificationPermission()
        }
        setupWindowAndAccent()
        setupNotificationWork()
        setupFirebaseMessaging()
        binding = ActivityHostLoggedInBinding.inflate(layoutInflater)
        setupNavHeaderView()
        setContentView(binding.root)
        observeViewModels()
        setSupportActionBar(binding.toolbarLogin)
        setupNavigationViews()
        setupNavController()
        setupHeaderImages()
        setupHeaderTag()
        setFYBConfiguration()

    }

    private fun setFYBConfiguration() {
        viewModel.getFybScreenSettings()
    }

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkNotificationPermission() {
        val permission = Manifest.permission.POST_NOTIFICATIONS
        when {
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                // make your action here
            }

            shouldShowRequestPermissionRationale(permission) -> {
                Toast.makeText(
                    this,
                    "You need to enable permission from your settings in order to receive notifications",
                    Toast.LENGTH_SHORT
                ).show()
                // permission denied permanently
            }

            else -> {
                requestNotificationPermission.launch(permission)
            }
        }
    }

    private fun observeViewModels() {
        viewModel.headerTag.observe(this) { headerTag ->
            if (headerTag.text.isEmpty()) {
                navHeaderBinding.headerPictureTag.visibility = View.GONE
            } else {
                navHeaderBinding.headerPictureTag.visibility = View.VISIBLE
                navHeaderBinding.headerPictureTag.text = headerTag.text
            }
        }

        viewModel.fybScreenSettings.observe(this) { fybSettings ->
            Timber.d("Fyb Screen Settings: :$fybSettings")
            when (fybSettings.drawerItemVisible) {
                0 -> binding.navView.menu.findItem(R.id.fyb)?.isVisible = false
                1 -> binding.navView.menu.findItem(R.id.fyb)?.isVisible = true
                else -> binding.navView.menu.findItem(R.id.fyb)?.isVisible = false
            }

            binding.navView.menu.findItem(R.id.fyb)?.title = fybSettings.drawerItemTitle

        }
    }

    private fun setupHeaderTag() {
        viewModel.getHeaderTag()

    }


    private fun setupNavHeaderView() {
        val headerView: View = binding.navView.getHeaderView(0)
        navHeaderBinding = NavHeaderBinding.bind(headerView)
    }

    private fun setupHeaderImages() {
        viewModel.getHeaderImagesFromFirebaseStorage()
        viewModel.headerImages.observe(this) { headerImages ->
            if (headerImages.isNotEmpty()) {
                navHeaderBinding.viewPagerNavHeader.visibility = View.VISIBLE
                navHeaderBinding.headerPictureTag.visibility = View.VISIBLE
                isAutoScrollPaused = false
                navHeaderBinding.fcsLogo.visibility = View.GONE
                setupViewPager(headerImages)
                startAutoScroll() // Start auto scroll
            } else {
                navHeaderBinding.viewPagerNavHeader.visibility = View.GONE
                navHeaderBinding.headerPictureTag.visibility = View.GONE
                isAutoScrollPaused = true
                navHeaderBinding.fcsLogo.visibility = View.VISIBLE
                navHeaderBinding.fcsLogo.setImageResource(R.drawable.fcs_logo)

            }


        }

    }

    private fun setupViewPager(headerImages: List<HeaderImage>) {
        headerImageAdapter = HeaderImageAdapter()
        navHeaderBinding.viewPagerNavHeader.adapter = headerImageAdapter
        navHeaderBinding.viewPagerNavHeader.rotationY = 180F

        val compositePageTransformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
            addTransformer { page, position ->
                val rotation = -position * 30 // Rotation angle
                page.rotation = rotation

                // Hide adjacent pages' edges
                if (position < -0.5 || position > 0.5) {
                    page.alpha = 0f
                } else {
                    page.alpha = 1f
                }
            }
        }

        navHeaderBinding.viewPagerNavHeader.apply {
            setPageTransformer(compositePageTransformer)
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
        headerImageAdapter.updateImages(headerImages) // Update the adapter's image list
    }

    private fun setupNavController() {
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

    private fun setupNavigationViews() {
        binding.navView.setNavigationItemSelectedListener(this)

    }

    private fun setupFirebaseMessaging() {
        FirebaseMessaging.getInstance().apply {
            subscribeToTopic("dailyBibleVerse")
            subscribeToTopic("announcements")
        }

    }

    private fun setupNotificationWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val notificationWork = OneTimeWorkRequestBuilder<DailyBibleVerseWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext)
            .enqueue(
                notificationWork
            )
    }

    private fun setupWindowAndAccent() {
        setAccentColour(Prefs(this).Settings().accent)
        val isDarkMode = inDarkMode(this)
        window.apply {
            val statusBarCol = if (isDarkMode) Color.BLACK else Color.WHITE
            setStatusBarColor(statusBarCol, !isDarkMode)
            updateNavbarColour()
        }
    }

    private fun startAutoScroll() {
        autoScrollJob?.cancel() // Cancel any existing auto scroll job
        autoScrollJob = autoScrollCoroutineScope.launch {
            while (isActive) {
                if (!isAutoScrollPaused) {
                    delay(4000)
                    val currentItem = navHeaderBinding.viewPagerNavHeader.currentItem
                    val nextItem = currentItem + 1
                    navHeaderBinding.viewPagerNavHeader.setCurrentItem(nextItem, true)
                } else {
                    delay(1000)
                }
            }
        }
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
            R.id.fyb -> {
                navController.navigate(R.id.navigation_fyb)
            }

            R.id.announcement -> {
                if (navController.currentDestination?.id == R.id.navigation_announcement) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                } else if (navController.currentDestination?.id == R.id.navigation_excos) {
                    navController.popBackStack(R.id.navigation_excos, true)
                }
            }

            R.id.daily_guide -> navController.navigate(R.id.navigation_daily_guide)

            R.id.daily_bible_verse -> navController.navigate(R.id.navigation_daily_bible_verse)
            R.id.songs -> navController.navigate(R.id.navigation_songs)
            R.id.admin_login -> navController.navigate(R.id.nav_graph_admin)
            R.id.contact_us -> dialogsNavigator.showContactusDialog()
            R.id.donate -> dialogsNavigator.showDonateDialog()
            R.id.rate_me -> {
                try {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data =
                            Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
                        setPackage("com.android.vending")
                    }
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                        )
                    )
                }

            }
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
            isAutoScrollPaused = true

        } else {
            super.onBackPressed()
        }

    }

    override fun onResume() {
        super.onResume()
        isAutoScrollPaused = false
        startAutoScroll()
    }

    override fun onPause() {
        super.onPause()
        isAutoScrollPaused = true
    }

    override fun onDestroy() {
        super.onDestroy()
        autoScrollJob?.cancel()
        autoScrollCoroutineScope.cancel()
    }


}