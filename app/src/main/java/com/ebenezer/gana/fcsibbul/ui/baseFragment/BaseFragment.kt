package com.ebenezer.gana.fcsibbul.ui.baseFragment

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.ui.host.HostActivityLoggedIn
import com.ebenezer.gana.fcsibbul.utils.Constants
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment : Fragment() {
    protected open var bottomNavigationViewVisibility = View.VISIBLE

    // The drawer state should be unlocked by default.
    // Fragment which inherits from BaseFragment can make adjustments
    protected open var drawerState = Constants.DRAWER_STATE_UNLOCKED

    /**
     * Creates and show the snack bar
     * @param message the message to display on the snack bar
     * @param isError changes the color of the snackBar if true or false
     */
    fun showSnackBar(message: String, isError: Boolean) {
        val snackBar = Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            message, Snackbar.LENGTH_LONG
        )
        val snackBarView = snackBar.view
        if (isError) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorSnackBarError
                )
            )
        } else {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(), R.color.colorSnackBarSuccess
                )
            )
        }
        snackBar.show()
    }

    override fun onResume() {
        super.onResume()
        if (activity is HostActivityLoggedIn) {
            val hostActivityLoggedIn = activity as HostActivityLoggedIn
            hostActivityLoggedIn.setBottomNavigationVisibility(bottomNavigationViewVisibility)
            hostActivityLoggedIn.setDrawerLockedState(drawerState)
        }

    }


}