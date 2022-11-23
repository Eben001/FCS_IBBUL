package com.ebenezer.gana.fcsibbul.di

import com.ebenezer.gana.fcsibbul.ui.dialogs.DialogsNavigator
import com.ebenezer.gana.fcsibbul.ui.host.HostActivityLoggedIn
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val activityModule = module {
    scope<HostActivityLoggedIn> {
        scoped { DialogsNavigator(androidContext()) }
    }
}