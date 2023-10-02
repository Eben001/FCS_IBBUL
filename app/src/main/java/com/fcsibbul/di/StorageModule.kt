package com.fcsibbul.di

import com.google.firebase.storage.FirebaseStorage
import org.koin.dsl.module


val storageModule = module {
    single { FirebaseStorage.getInstance() }
}