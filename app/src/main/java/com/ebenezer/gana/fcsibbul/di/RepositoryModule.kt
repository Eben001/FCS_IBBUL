package com.ebenezer.gana.fcsibbul.di

import com.ebenezer.gana.fcsibbul.data.repository.FcsRepository
import com.ebenezer.gana.fcsibbul.data.repository.announcement.AnnouncementDetailsRepository
import com.ebenezer.gana.fcsibbul.data.repository.announcement.AnnouncementRepository
import com.ebenezer.gana.fcsibbul.data.repository.bibleVerse.BibleVerseRepository
import com.ebenezer.gana.fcsibbul.data.repository.song.SongRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAnnouncementRepo(firestore: FirebaseFirestore) =
        AnnouncementRepository(firestore)

    @Provides
    @Singleton
    fun provideAnnouncementDetailsRepo(firestore: FirebaseFirestore) =
        AnnouncementDetailsRepository(firestore)

    @Provides
    @Singleton
    fun provideFcsRepo(firebaseAuth: FirebaseAuth, firestore: FirebaseFirestore) =
        FcsRepository(firebaseAuth, firestore)

    @Provides
    @Singleton
    fun provideSongsRepo(firebaseAuth: FirebaseAuth, firestore: FirebaseFirestore) = SongRepository(firebaseAuth,firestore)

     @Provides
    @Singleton
    fun provideDailyBibleVerseRepo(firebaseAuth: FirebaseAuth, firestore: FirebaseFirestore) = BibleVerseRepository(firebaseAuth,firestore)


}