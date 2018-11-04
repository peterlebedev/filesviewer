package cz.lebedev.filesviewer.di

import cz.lebedev.filesviewer.view.ui.DirectoryFragment
import cz.lebedev.filesviewer.view.ui.DirectoryPreference
import dagger.Component
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentsModule {
    @ContributesAndroidInjector
    abstract fun contributeDirectoryFragment() : DirectoryFragment

    @ContributesAndroidInjector
    abstract fun contributeDirectoryPreference() : DirectoryPreference

}