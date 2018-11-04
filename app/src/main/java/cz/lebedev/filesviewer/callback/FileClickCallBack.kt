package cz.lebedev.filesviewer.callback

import android.view.View
import java.io.File

interface FileClickCallback {
    fun onClick(file: File)
    fun onLongClick(view: View,file :File) : Boolean
}

