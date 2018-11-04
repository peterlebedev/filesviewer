package cz.lebedev.filesviewer.view.adapters

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import cz.lebedev.filesviewer.R
import cz.lebedev.filesviewer.isUpFileNameFake
import java.io.File

@BindingAdapter("fileOrFolder")
fun fileOrFolder(view: ImageView, file: File) {

    if(file.absolutePath.isUpFileNameFake())
        view.visibility = View.GONE
    else if (file.isDirectory)
        view.setImageResource(R.drawable.folder)
    else
        view.setImageResource(R.drawable.file)
}