package cz.lebedev.filesviewer.view.adapters

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import cz.lebedev.filesviewer.R

@BindingAdapter("errorMessage")
fun errorMessage(view: TextView, error: String?) {
    if(error==null || error.length==0){
        view.text = view.context.getString(R.string.loading)
    }else{
        view.text = error
    }
}