package com.fcsibbul.di

import com.fcsibbul.ui.dialogs.DialogsNavigator
import com.fcsibbul.ui.host.HostActivityLoggedIn
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val activityModule = module {
    scope<HostActivityLoggedIn> {
        scoped { DialogsNavigator(androidContext()) }
    }
}