package com.fcsibbul.ui

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.fcsibbul.R
import com.fcsibbul.data.network.ConnectivityObserver
import com.fcsibbul.data.network.NetworkConnectivityObserver
import com.fcsibbul.databinding.FragmentFYBBinding
import com.fcsibbul.ui.baseFragment.BaseFragment
import com.fcsibbul.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.Rotation
import nl.dionsegijn.konfetti.core.Spread
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.models.Size
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit
import kotlin.random.Random


class FYBFragment : BaseFragment(), SensorEventListener {

    override var bottomNavigationViewVisibility = View.GONE
    override var drawerState = Constants.DRAWER_STATE_LOCKED_CLOSED
    private val colors = arrayOf(
        Color.parseColor("#FFCDD2"), // Light Red
        Color.parseColor("#E1FFD5"), // Light Green
        Color.parseColor("#BBDEFB"), // Light Blue
        Color.parseColor("#B2EBF2"), // Light Cyan
        Color.parseColor("#F8BBD0"), // Light Magenta
        Color.parseColor("#FFF9C4")  // Light Yellow
    )
    private val autoScrollCoroutineScope = CoroutineScope(Dispatchers.Main)
    private var autoScrollJob: Job? = null
    private var isAutoScrollPaused = false
    private lateinit var fybImageAdapter: FybImageAdapter
    private lateinit var skeleton: Skeleton // Declare the skeleton variable

    private val viewModel: FYBViewModel by viewModel()
    private val networkConnectivityObserver by inject<NetworkConnectivityObserver>()


    private lateinit var sensorManager: SensorManager
    private var accelSensor: Sensor? = null
    private var _binding: FragmentFYBBinding? = null
    private val binding get() = _binding!!

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (event.values[0] > 0) {
                binding.background.setBackgroundColor(colors[Random.nextInt(6)])
            }
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFYBBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getFybSettings()

        viewModel.fybSettings.observe(viewLifecycleOwner) { fybSettings ->
            (requireActivity() as AppCompatActivity).supportActionBar?.title =
                fybSettings.screenTitle

        }
        sensorManager = requireContext().getSystemService<SensorManager>()!!
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        binding.konfettiView.start(festive())
        viewLifecycleOwner.lifecycleScope.launch {
            while (true) {
                delay(10000)
                val randomNumber = generateRandomNumber()
                binding.konfettiView.start(
                    when (randomNumber) {
                        0 -> festive()
                        1 -> explode()
                        2 -> parade()
                        3 -> rain()
                        else -> festive()

                    }
                )

            }
        }
        applySkeletonToViewPager2()
        networkConnectivityObserver.observe().onEach { status ->
            when (status) {
                ConnectivityObserver.Status.Available -> getFybImageUrlsFromFirestore()
                ConnectivityObserver.Status.Unavailable -> applySkeletonToViewPager2()
                else -> {}
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }

    private fun getFybImageUrlsFromFirestore() {
        viewModel.getFybImageUrlsFromFirestore()
        viewModel.fybImages.observe(viewLifecycleOwner) { welcomeScreenImages ->

            fybImageAdapter = FybImageAdapter()
            binding.viewPager.adapter = fybImageAdapter

           /* val compositePageTransformer = CompositePageTransformer().apply {
                addTransformer(MarginPageTransformer(40))
                addTransformer { page, position ->
                    val r = 1 - abs(position)
                    page.scaleY = 0.85f + r * 0.15f
                }
            }*/
            binding.viewPager.setPageTransformer(CubeInScalingTransformer())
            binding.viewPager.clipToPadding = false
            binding.viewPager.clipChildren = false
            binding.viewPager.offscreenPageLimit = 3
            binding.viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            fybImageAdapter.updateImages(welcomeScreenImages) // Update the adapter's image list

            startAutoScroll() // Start auto scroll

        }
    }

    private fun startAutoScroll() {
        autoScrollJob?.cancel() // Cancel any existing auto scroll job
        autoScrollJob = autoScrollCoroutineScope.launch {
            while (isActive) {
                if (!isAutoScrollPaused) {
                    delay(9000)
                    val currentItem = binding.viewPager.currentItem
                    val nextItem = currentItem + 1
                    binding.viewPager.setCurrentItem(nextItem, true)
                } else {
                    delay(1000)
                }
            }
        }
    }

    private fun applySkeletonToViewPager2() {
        skeleton = binding.viewPager.applySkeleton(R.layout.item_welcome_image)
        skeleton.showSkeleton()
    }

    private fun generateRandomNumber(): Int {
        // Replace 4 with the number of available Konfetti animations
        return (0 until 4).random()
    }

    private fun festive(): List<Party> {
        val party = Party(
            speed = 30f,
            maxSpeed = 50f,
            damping = 0.9f,
            angle = Angle.TOP,
            spread = 45,
            size = listOf(Size.SMALL, Size.LARGE),
            timeToLive = 3000L,
            rotation = Rotation(),
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(30),
            position = Position.Relative(0.5, 1.0)
        )

        return listOf(
            party,
            party.copy(
                speed = 55f,
                maxSpeed = 65f,
                spread = 10,
                emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(10),
            ),
            party.copy(
                speed = 50f,
                maxSpeed = 60f,
                spread = 120,
                emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(40),
            ),
            party.copy(
                speed = 65f,
                maxSpeed = 80f,
                spread = 10,
                emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(10),
            )
        )
    }

    private fun explode(): List<Party> {
        return listOf(
            Party(
                speed = 0f,
                maxSpeed = 30f,
                damping = 0.9f,
                spread = 360,
                colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100),
                position = Position.Relative(0.5, 0.3)
            )
        )
    }

    private fun parade(): List<Party> {
        val party = Party(
            speed = 10f,
            maxSpeed = 30f,
            damping = 0.9f,
            angle = Angle.RIGHT - 45,
            spread = Spread.SMALL,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            emitter = Emitter(duration = 5, TimeUnit.SECONDS).perSecond(30),
            position = Position.Relative(0.0, 0.5)
        )

        return listOf(
            party,
            party.copy(
                angle = party.angle - 90, // flip angle from right to left
                position = Position.Relative(1.0, 0.5)
            ),
        )
    }

    private fun rain(): List<Party> {
        return listOf(
            Party(
                speed = 0f,
                maxSpeed = 15f,
                damping = 0.9f,
                angle = Angle.BOTTOM,
                spread = Spread.ROUND,
                colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                emitter = Emitter(duration = 5, TimeUnit.SECONDS).perSecond(100),
                position = Position.Relative(0.0, 0.0).between(Position.Relative(1.0, 0.0))
            )
        )
    }


    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            this, accelSensor, 1000 * 1000
        )
    }

    override fun onPause() {
        sensorManager.unregisterListener(this)
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
        autoScrollJob?.cancel()
        autoScrollCoroutineScope.cancel()
        _binding = null
    }


}