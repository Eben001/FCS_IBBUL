package com.ebenezer.gana.fcsibbul.ui.baseFragment

import android.view.View
import androidx.fragment.app.Fragment
import com.ebenezer.gana.fcsibbul.ui.host.HostActivityLoggedIn
import com.ebenezer.gana.fcsibbul.utils.Constants

abstract class BaseFragment:Fragment() {
    protected open var bottomNavigationViewVisibility = View.VISIBLE
    // The drawer state should be unlocked by default.
    // Fragment which inherits from BaseFragment can make adjustments
    protected open var drawerState = Constants.DRAWER_STATE_UNLOCKED

    override fun onResume() {
        super.onResume()
        if(activity is HostActivityLoggedIn){
            val hostActivityLoggedIn = activity as HostActivityLoggedIn
            hostActivityLoggedIn.setBottomNavigationVisibility(bottomNavigationViewVisibility)
            hostActivityLoggedIn.setDrawerLockedState(drawerState)
        }

    }



}