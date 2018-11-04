package cz.lebedev.filesviewer.di

import cz.lebedev.filesviewer.view.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
public abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = arrayOf(FragmentsModule::class))
    abstract fun contributeMainActivity() : MainActivity


}