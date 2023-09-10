package com.ebenezer.gana.fcsibbul.di

import com.ebenezer.gana.fcsibbul.ui.admin.addExco.AddExcoViewModel
import com.ebenezer.gana.fcsibbul.ui.admin.dashboard.AdminDashboardViewModel
import com.ebenezer.gana.fcsibbul.ui.admin.postAnnouncement.PostAnnouncementViewModel
import com.ebenezer.gana.fcsibbul.ui.admin.postBibleVerse.PostBibleVerseViewModel
import com.ebenezer.gana.fcsibbul.ui.admin.postSong.PostSongViewModel
import com.ebenezer.gana.fcsibbul.ui.announcement.announcementDetails.AnnouncementDetailsViewModel
import com.ebenezer.gana.fcsibbul.ui.announcement.announcementList.AnnouncementListViewModel
import com.ebenezer.gana.fcsibbul.ui.dailyVerse.DailyBibleVerseViewModel
import com.ebenezer.gana.fcsibbul.ui.excos.excosList.ExcosViewModel
import com.ebenezer.gana.fcsibbul.ui.host.HostActivityLoggedInViewModel
import com.ebenezer.gana.fcsibbul.ui.login.LoginViewModel
import com.ebenezer.gana.fcsibbul.ui.settings.SettingsViewModel
import com.ebenezer.gana.fcsibbul.ui.signup.SignUpViewModel
import com.ebenezer.gana.fcsibbul.ui.songs.SongsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel {AnnouncementListViewModel(get()) }
    viewModel {DailyBibleVerseViewModel(get()) }
    viewModel {ExcosViewModel(get()) }
    viewModel {LoginViewModel(get()) }
    viewModel {SettingsViewModel(get()) }
    viewModel {SignUpViewModel(get())}
    viewModel {SongsViewModel(get()) }
    viewModel {AnnouncementDetailsViewModel(get())}
    viewModel {PostSongViewModel(get())}
    viewModel {PostBibleVerseViewModel(get())}
    viewModel {PostAnnouncementViewModel(get(), get())}
    viewModel {AdminDashboardViewModel(get()) }
    viewModel { AddExcoViewModel(get()) }
    viewModel { HostActivityLoggedInViewModel(get()) }
}