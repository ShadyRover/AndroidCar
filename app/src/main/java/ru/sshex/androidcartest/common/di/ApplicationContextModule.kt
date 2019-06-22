package ru.sshex.androidcartest.common.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ApplicationContextModule(private val applicationContext: Context) {

    @Provides
    fun provideApplicationContext() = applicationContext
}