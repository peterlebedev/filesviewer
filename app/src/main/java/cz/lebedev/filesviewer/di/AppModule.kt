package cz.lebedev.filesviewer.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.lifecycle.ViewModelProvider
import cz.lebedev.filesviewer.Env
import cz.lebedev.filesviewer.service.repository.DirectorySharedPrefs

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(subcomponents = arrayOf(ViewModelSubComponent::class))
class AppModule{


    @Singleton
    @Provides
    fun provideShared(application : Application) : DirectorySharedPrefs {
        return DirectorySharedPrefs(application)
    }


    @Singleton
    @Provides
    fun provideViewModelFactory(
            builder: ViewModelSubComponent.Builder): ViewModelProvider.Factory {
        return ViewModelFactory(builder.build())
    }



}