package com.fcsibbul.di

import com.fcsibbul.data.repository.FcsRepository
import com.fcsibbul.data.repository.announcement.AnnouncementDetailsRepository
import com.fcsibbul.data.repository.announcement.AnnouncementRepository
import com.fcsibbul.data.repository.bibleVerse.BibleVerseRepository
import com.fcsibbul.data.repository.excos.ExcosRepository
import com.fcsibbul.data.repository.song.SongRepository
import org.koin.dsl.module


val repositoryModule = module {
    single { AnnouncementRepository(get()) }
    single {AnnouncementDetailsRepository(get())}
    single {ExcosRepository(get(),get())}
    single {FcsRepository(get(), get())}
    single {SongRepository(get(), get())}
    single {BibleVerseRepository(get(), get(), get())}
}
