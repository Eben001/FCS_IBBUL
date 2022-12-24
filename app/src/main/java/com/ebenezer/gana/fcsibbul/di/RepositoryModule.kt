package com.ebenezer.gana.fcsibbul.di

import com.ebenezer.gana.fcsibbul.data.repository.FcsRepository
import com.ebenezer.gana.fcsibbul.data.repository.announcement.AnnouncementDetailsRepository
import com.ebenezer.gana.fcsibbul.data.repository.announcement.AnnouncementRepository
import com.ebenezer.gana.fcsibbul.data.repository.bibleVerse.BibleVerseRepository
import com.ebenezer.gana.fcsibbul.data.repository.excos.ExcosRepository
import com.ebenezer.gana.fcsibbul.data.repository.song.SongRepository
import org.koin.dsl.module


val repositoryModule = module {
    single { AnnouncementRepository(get()) }
    single {AnnouncementDetailsRepository(get())}
    single {ExcosRepository(get(),get())}
    single {FcsRepository(get(), get())}
    single {SongRepository(get(), get())}
    single {BibleVerseRepository(get(), get(), get())}
}
