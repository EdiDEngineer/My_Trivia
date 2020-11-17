package edu.utap.mytrivia.ui.home.fragment.home.adapter

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView


@BindingAdapter("listChoiceData")
fun RecyclerView.bindChoiceRecyclerView(data: List<String>?) {
    val adapter = adapter as QuizChoicesListAdapter
    adapter.apply {
        addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
            drawable?.colorFilter = PorterDuffColorFilter(
                ContextCompat.getColor(context, android.R.color.darker_gray),
                PorterDuff.Mode.SRC
            )
        })
        submitList(data)
    }
}
