package cz.lebedev.filesviewer.view.adapters

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visibleGone")
fun visibleGone(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}