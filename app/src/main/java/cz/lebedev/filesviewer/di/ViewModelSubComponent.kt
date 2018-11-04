package cz.lebedev.filesviewer.di

import android.content.Context
import cz.lebedev.filesviewer.viewModel.DirectoryViewModel
import dagger.Subcomponent

@Subcomponent
interface ViewModelSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): ViewModelSubComponent
    }

    fun directoryViewModel(): DirectoryViewModel
}
