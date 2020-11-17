package edu.utap.mytrivia.ui.home.fragment.dashboard.adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.utap.mytrivia.R
import edu.utap.mytrivia.data.local.entity.Quiz
import java.util.*
import java.util.concurrent.TimeUnit

@BindingAdapter("listQuizData")
fun RecyclerView.bindQuizRecyclerView(data: List<Quiz>?) {
    val adapter = adapter as QuizScoreListAdapter
    adapter.submitList(data) {
        scrollToPosition(0)
    }
}

@BindingAdapter("timeAgo")
fun TextView.timeAgo(date: Date) {
    val diff = Date().time - date.time
    val seconds = TimeUnit.SECONDS.convert(diff, TimeUnit.MILLISECONDS)
    val minutes = TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS)
    val hours = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS)
    val days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
    val years = days / 365
    val words = when {
        seconds < 60 -> {
            resources.getString(R.string.time_ago_seconds, seconds.toInt())
        }
        minutes < 60 -> {
            resources.getString(R.string.time_ago_minutes, minutes.toInt())
        }
        hours < 24 -> {
            resources.getString(R.string.time_ago_hours, hours.toInt())
        }
        days < 365 -> {
            resources.getString(R.string.time_ago_days, days.toInt())
        }
        else -> {
            resources.getString(R.string.time_ago_years, years.toInt())
        }
    }
    text = words
}