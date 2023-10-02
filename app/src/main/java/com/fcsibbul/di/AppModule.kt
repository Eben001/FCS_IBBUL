package com.fcsibbul.di

import com.fcsibbul.ui.admin.addExco.AddExcoViewModel
import com.fcsibbul.ui.admin.dashboard.AdminDashboardViewModel
import com.fcsibbul.ui.admin.postAnnouncement.PostAnnouncementViewModel
import com.fcsibbul.ui.admin.postBibleVerse.PostBibleVerseViewModel
import com.fcsibbul.ui.admin.postSong.PostSongViewModel
import com.fcsibbul.ui.announcement.announcementDetails.AnnouncementDetailsViewModel
import com.fcsibbul.ui.announcement.announcementList.AnnouncementListViewModel
import com.fcsibbul.ui.dailyVerse.DailyBibleVerseViewModel
import com.fcsibbul.ui.excos.excosList.ExcosViewModel
import com.fcsibbul.ui.host.HostActivityLoggedInViewModel
import com.fcsibbul.ui.login.LoginViewModel
import com.fcsibbul.ui.settings.SettingsViewModel
import com.fcsibbul.ui.signup.SignUpViewModel
import com.fcsibbul.ui.songs.SongsViewModel
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