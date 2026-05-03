package com.fcsibbul.ui.signup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.fcsibbul.R
import com.fcsibbul.data.network.ConnectivityObserver
import com.fcsibbul.data.network.NetworkConnectivityObserver
import com.fcsibbul.databinding.SignUpFragmentBinding
import com.fcsibbul.ui.baseFragment.BaseFragment
import com.fcsibbul.ui.host.HostActivityLoggedIn
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import kotlin.math.abs

class SignUpFragment : BaseFragment() {

    private val autoScrollCoroutineScope = CoroutineScope(Dispatchers.Main)
    private var autoScrollJob: Job? = null
    private var isAutoScrollPaused = false
    private lateinit var welcomeImageAdapter: WelcomeImageAdapter
    private lateinit var skeleton: Skeleton // Declare the skeleton variable

    private var _binding: SignUpFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignUpViewModel by viewModel()
    private lateinit var googleSignInClient: GoogleSignInClient

    private val networkConnectivityObserver by inject<NetworkConnectivityObserver>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SignUpFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val privacyPolicyFullText = resources.getString(R.string.by_signing_up_you_agree_to_the_terms_outlined_in_our_privacy_policy)
        val spannableString = SpannableString(privacyPolicyFullText)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Handle the click event, open the privacy policy URL in a web browser
                val privacyPolicyUrl = resources.getString(R.string.privacy_policy_link)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(privacyPolicyUrl))
                startActivity(intent)
            }
        }
        val startIndex = privacyPolicyFullText.indexOf("privacy policy")
        spannableString.setSpan(clickableSpan, startIndex, startIndex + "privacy policy".length, 0)
        binding.txtPrivacyPolicy.text = spannableString
        binding.txtPrivacyPolicy.movementMethod = LinkMovementMethod.getInstance()



        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(resources.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions)

        setOnClickListeners()
        applySkeletonToViewPager2()

        networkConnectivityObserver.observe().onEach {status->
            when(status){
                ConnectivityObserver.Status.Available -> getImageUrlsFromFirebaseStorage()
                ConnectivityObserver.Status.Unavailable -> applySkeletonToViewPager2()
                else -> {}
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        observeViewModels()


    }
    private fun applySkeletonToViewPager2() {
        skeleton = binding.viewPager.applySkeleton(R.layout.item_welcome_image)
        skeleton.showSkeleton()
    }

    private fun observeViewModels() {

        viewModel.isSignupSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                val intent = Intent(requireActivity(), HostActivityLoggedIn::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        viewModel.result.observe(viewLifecycleOwner) {
            if (viewModel.isSignupSuccess.value == true) {
                showSnackBar(requireView(),it.asString(requireContext()), false)
            } else {
                showSnackBar(requireView(),it.asString(requireContext()), true)
            }

        }


    }

    private fun setOnClickListeners() {
        binding.btnRegister.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private fun getImageUrlsFromFirebaseStorage() {
        viewModel.getWelcomeImagesFromFirebaseStorage()
        viewModel.welcomeScreenImages.observe(viewLifecycleOwner) { welcomeScreenImages ->

            welcomeImageAdapter = WelcomeImageAdapter()
            binding.viewPager.adapter = welcomeImageAdapter

            val compositePageTransformer = CompositePageTransformer().apply {
                addTransformer(MarginPageTransformer(40))
                addTransformer { page, position ->
                    val r = 1 - abs(position)
                    page.scaleY = 0.85f + r * 0.15f
                }
            }
            binding.viewPager.setPageTransformer(compositePageTransformer)
            binding.viewPager.clipToPadding = false
            binding.viewPager.clipChildren = false
            binding.viewPager.offscreenPageLimit = 3
            binding.viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            welcomeImageAdapter.updateImages(welcomeScreenImages) // Update the adapter's image list

            startAutoScroll() // Start auto scroll

        }
    }


    private fun startAutoScroll() {
        autoScrollJob?.cancel() // Cancel any existing auto scroll job
        autoScrollJob = autoScrollCoroutineScope.launch {
            while (isActive) {
                if (!isAutoScrollPaused) {
                    delay(4000)
                    val currentItem = binding.viewPager.currentItem
                    val nextItem = currentItem + 1
                    binding.viewPager.setCurrentItem(nextItem, true)
                } else {
                    delay(1000)
                }
            }
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Timber.d("Sign in result code: ${result.resultCode}")
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(com.google.android.gms.common.api.ApiException::class.java)
                Timber.d("Sign in successful: ${account.email}")
            } catch (e: com.google.android.gms.common.api.ApiException) {
                Timber.e("Sign in failed. Code: ${e.statusCode}, Message: ${e.message}")
            }
            viewModel.handleSigningTask(task)
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
        _binding = null
    }

}