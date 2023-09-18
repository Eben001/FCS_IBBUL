package com.ebenezer.gana.fcsibbul.ui.host

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.core.Bars.updateNavbarColour
import com.ebenezer.gana.fcsibbul.core.setDarkStatusIcons
import com.ebenezer.gana.fcsibbul.data.models.HeaderImage
import com.ebenezer.gana.fcsibbul.data.notification.DailyBibleVerseWorker
import com.ebenezer.gana.fcsibbul.databinding.ActivityHostLoggedInBinding
import com.ebenezer.gana.fcsibbul.databinding.NavHeaderBinding
import com.ebenezer.gana.fcsibbul.ui.common.Accent.setAccentColour
import com.ebenezer.gana.fcsibbul.ui.common.Prefs
import com.ebenezer.gana.fcsibbul.ui.dialogs.DialogsNavigator
import com.google.android.gms.ads.AdRequest
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

        setupWindowAndAccent()
        setupNotificationWork()
        setupFirebaseMessaging()
        binding = ActivityHostLoggedInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeViewModels()
        setSupportActionBar(binding.toolbarLogin)
        setupNavigationViews()
        setupNavController()
        setupHeaderImages()
        setupHeaderTag()
        setupNavHeaderView()
        initializeAds()


    }

    private fun observeViewModels() {
        viewModel.headerTag.observe(this){headerTag->
            if(headerTag.text.isEmpty()){
                navHeaderBinding.headerPictureTag.visibility = View.GONE
            }else{
                navHeaderBinding.headerPictureTag.visibility = View.VISIBLE
                navHeaderBinding.headerPictureTag.text = headerTag.text
            }
        }    }

    private fun setupHeaderTag() {
        viewModel.getHeaderTag()

    }

    private fun initializeAds() {
        val adRequest = AdRequest.Builder().build()
        navHeaderBinding.adView.loadAd(adRequest)

    }

    private fun setupNavHeaderView() {
        val headerView: View = binding.navView.getHeaderView(0)
        navHeaderBinding = NavHeaderBinding.bind(headerView)
    }

    private fun setupHeaderImages() {
        viewModel.getHeaderImagesFromFirebaseStorage()
        viewModel.headerImages.observe(this) { headerImages ->
            setupViewPager(headerImages)
            startAutoScroll() // Start auto scroll

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
            .setRequiredNetworkType(NetworkType.UNMETERED)
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
        window.apply {
            statusBarColor = Color.TRANSPARENT
            updateNavbarColour()
            setDarkStatusIcons()
            /*navigationBarColor =
                ContextCompat.getColor(context, R.color.abbBackgroundColor)*/
            //WindowCompat.setDecorFitsSystemWindows(this, false)
        }
        setAccentColour(Prefs(this).Settings().accent)
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