package com.fcsibbul.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.fcsibbul.R
import com.fcsibbul.data.models.FeedbackData
import com.fcsibbul.ui.common.AccentColor
import com.fcsibbul.ui.common.AccentSetting
import com.fcsibbul.ui.common.Feedback
import com.fcsibbul.ui.common.Prefs
import com.fcsibbul.ui.host.HostActivityLoggedIn
import com.fcsibbul.ui.host.HostActivityLoggedOut
import com.fcsibbul.utils.Constants.DRAWER_STATE_LOCKED_CLOSED
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.maxkeppeler.sheets.color.ColorSheet
import com.maxkeppeler.sheets.color.ColorView
import com.maxkeppeler.sheets.core.SheetStyle
import com.maxkeppeler.sheets.info.InfoSheet
import com.maxkeppeler.sheets.input.InputSheet
import com.maxkeppeler.sheets.input.type.InputEditText
import com.maxkeppeler.sheets.options.DisplayMode
import com.maxkeppeler.sheets.options.Option
import com.maxkeppeler.sheets.options.OptionsSheet
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : PreferenceFragmentCompat() {


    private val viewModel: SettingsViewModel by viewModel()

    private var userInput: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as HostActivityLoggedIn).setBottomNavigationVisibility(View.GONE)
        (activity as HostActivityLoggedIn).setDrawerLockedState(DRAWER_STATE_LOCKED_CLOSED)

        observeViewModels()

    }
    private fun observeViewModels() {
        viewModel.result.observe(viewLifecycleOwner) {
            if (viewModel.isFeedbackSubmitSuccess.value == true) {
                Toast.makeText(requireContext(), it.asString(requireContext()), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), viewModel.result.value?.asString(requireContext()), Toast.LENGTH_SHORT).show()

            }
        }
    }
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val settings = Prefs(requireContext()).Settings()

        fun Preference.setThemeSummary() {
            val theme = settings.theme

            summary = getString(
                when (theme) {
                    MODE_NIGHT_NO -> R.string.light_mode
                    MODE_NIGHT_YES -> R.string.dark_mode
                    else -> R.string.follow_system_default
                }
            )

            icon = ContextCompat.getDrawable(
                context, when (theme) {
                    MODE_NIGHT_NO -> R.drawable.ic_light_mode
                    MODE_NIGHT_YES -> R.drawable.ic_dark_mode
                    else -> R.drawable.ic_dark_mode
                }
            )
        }

        fun Preference.setAccentSummary() {
            val accent = AccentSetting.getAccentColor(requireContext())

            summary = getString(
                when (accent) {
                    AccentColor.PINK -> R.string.pink
                    AccentColor.VIOLET -> R.string.violet
                    AccentColor.BLUE -> R.string.blue
                    AccentColor.LIGHT_BLUE -> R.string.lightBlue
                    AccentColor.TEAL -> R.string.teal
                    AccentColor.GREEN -> R.string.green
                    AccentColor.LIME -> R.string.lime
                    AccentColor.YELLOW -> R.string.yellow
                    AccentColor.ORANGE -> R.string.orange
                    AccentColor.RED -> R.string.red
                    AccentColor.CRIMSON -> R.string.crimson
                    else -> R.string.original
                }
            )

            icon = getDrawable(context, R.drawable.ic_circle)?.apply {
                setTint(
                    getColor(
                        context,
                        when (accent) {
                            AccentColor.PINK -> R.color.pinkAccent
                            AccentColor.VIOLET -> R.color.violetAccent
                            AccentColor.BLUE -> R.color.blueAccent
                            AccentColor.LIGHT_BLUE -> R.color.lightBlueAccent
                            AccentColor.TEAL -> R.color.tealAccent
                            AccentColor.GREEN -> R.color.greenAccent
                            AccentColor.LIME -> R.color.limeAccent
                            AccentColor.YELLOW -> R.color.yellowAccent
                            AccentColor.ORANGE -> R.color.orangeAccent
                            AccentColor.RED -> R.color.redAccent
                            AccentColor.CRIMSON -> R.color.crimsonAccent
                            else -> R.color.primaryColor
                        }
                    )
                )
            }
        }

        findPreference<Preference>("feedback")?.apply {
            //setThemeSummary()
            onPreferenceClickListener = Preference.OnPreferenceClickListener {
                it.isEnabled = true
                Feedback.virtualKey(requireView())
                InputSheet().show(context) {
                    title(R.string.str_feedback)
                    with(InputEditText {
                        required()
                        label(R.string.str_report_technical_issues_or_suggest_new_features)
                        hint(R.string.str_your_feedback_here)
                        //validationListener { value -> } // Add custom validation logic
                        changeListener { value -> userInput = value} // Input value changed
                        resultListener { value -> userInput = value } // Input value changed when form finished
                    })
                    onPositive(R.string.submit){
                        if (!userInput.isNullOrEmpty()) {
                            val userId:String? = null
                            viewModel.sendFeedback(FeedbackData(userId, userInput!!))
                        }
                    }
                }
                true
            }
        }
        findPreference<Preference>("logout")?.apply {
            //setThemeSummary()
            onPreferenceClickListener = Preference.OnPreferenceClickListener {
                it.isEnabled = false
                Feedback.virtualKey(requireView())
                InfoSheet().show(requireContext()){

                    style(SheetStyle.DIALOG)
                    title(R.string.log_out)
                    content(R.string.str_are_you_sure_you_want_to_logout)
                    onNegative(R.string.cancel) { Feedback.virtualKey(requireView()) }
                    onPositive(R.string.yes){
                        Firebase.auth.signOut()
                        val intent = Intent(requireContext(), HostActivityLoggedOut::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                    onClose { it.isEnabled = true }

                }
                true
            }
        }
        findPreference<Preference>("app_theme")?.apply {
            setThemeSummary()
            onPreferenceClickListener = Preference.OnPreferenceClickListener {
                it.isEnabled = false
                Feedback.virtualKey(requireView())
                OptionsSheet().show(requireContext()) {
                    style(SheetStyle.DIALOG)
                    displayToolbar(false)
                    displayMode(DisplayMode.LIST)
                    with(
                        Option(R.drawable.ic_light_mode, R.string.light_mode),
                        Option(R.drawable.ic_dark_mode, R.string.dark_mode),
                        Option(R.drawable.ic_dark_mode, R.string.follow_system_default)
                    )
                    onPositive { index: Int, _: Option ->
                        Feedback.confirm(view ?: return@onPositive)

                        settings.theme = when (index) {
                            0 -> MODE_NIGHT_NO
                            1 -> MODE_NIGHT_YES
                            else -> MODE_NIGHT_FOLLOW_SYSTEM
                        }
                        setDefaultNightMode(settings.theme)
                        setThemeSummary()
                    }
                    onClose { it.isEnabled = true }
                }
                true
            }
        }

        findPreference<Preference>("accent_color")?.apply {
            setAccentSummary()
            onPreferenceClickListener = Preference.OnPreferenceClickListener {
                isEnabled = false
                Feedback.virtualKey(requireView())
                ColorSheet().show(requireContext()) {
                    colorsRes(
                        mutableListOf(
                            R.color.pinkAccent,
                            R.color.violetAccent,
                            R.color.blueAccent,
                            R.color.lightBlueAccent,
                            R.color.tealAccent,
                            R.color.greenAccent,
                            R.color.limeAccent,
                            R.color.yellowAccent,
                            R.color.orangeAccent,
                            R.color.redAccent,
                            R.color.crimsonAccent,
                            R.color.primaryColor
                        )
                    )
                    style(SheetStyle.DIALOG)
                    defaultView(ColorView.TEMPLATE)
                    disableSwitchColorView()
                    onNegative(R.string.cancel) { Feedback.virtualKey(requireView()) }
                    onPositive(R.string.okay) { color ->
                        Feedback.confirm(view ?: return@onPositive)
                        AccentSetting.setAccentColorAndPref(requireContext(), color.toAccentColor())
                        setAccentSummary()
                    }
                    onClose { it.isEnabled = true }
                }
                true
            }
        }



    }

    private fun Int.toAccentColor(): AccentColor {
        return when (this) {
            getColor(requireContext(), R.color.pinkAccent) -> AccentColor.PINK
            getColor(requireContext(), R.color.violetAccent) -> AccentColor.VIOLET
            getColor(requireContext(), R.color.blueAccent) -> AccentColor.BLUE
            getColor(requireContext(), R.color.lightBlueAccent) -> AccentColor.LIGHT_BLUE
            getColor(requireContext(), R.color.tealAccent) -> AccentColor.TEAL
            getColor(requireContext(), R.color.greenAccent) -> AccentColor.GREEN
            getColor(requireContext(), R.color.limeAccent) -> AccentColor.LIME
            getColor(requireContext(), R.color.yellowAccent) -> AccentColor.YELLOW
            getColor(requireContext(), R.color.orangeAccent) -> AccentColor.ORANGE
            getColor(requireContext(), R.color.redAccent) -> AccentColor.RED
            getColor(requireContext(), R.color.crimsonAccent) -> AccentColor.CRIMSON
            else -> AccentColor.ORIGINAL
        }
    }



}