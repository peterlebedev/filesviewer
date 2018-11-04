package cz.lebedev.filesviewer.view.adapters

import android.graphics.Color
import android.view.View
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter

@BindingAdapter("selected")
fun selected(view: CardView, selected: Boolean) {
    view.setCardBackgroundColor(if (selected) Color.LTGRAY else Color.WHITE)
}