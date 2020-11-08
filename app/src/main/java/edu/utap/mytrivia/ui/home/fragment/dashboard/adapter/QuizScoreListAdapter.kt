package edu.utap.mytrivia.ui.home.fragment.dashboard.adapter

import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.utap.mytrivia.R
import edu.utap.mytrivia.data.local.entity.Quiz
import edu.utap.mytrivia.databinding.ItemQuizBinding
import edu.utap.mytrivia.ui.home.fragment.dashboard.adapter.QuizScoreListAdapter.QuizScoreListViewHolder.Companion.from


class QuizScoreListAdapter : ListAdapter<Quiz,
        QuizScoreListAdapter.QuizScoreListViewHolder>(QuizDiffCallback()) {

    override fun onBindViewHolder(holder: QuizScoreListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizScoreListViewHolder {
        return from(parent)
    }

    class QuizScoreListViewHolder private constructor(val binding: ItemQuizBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Quiz) {
            binding.quiz = item
            binding.score.text = "${item.score} (${item.maxScore})"
            val categoryString = SpannableStringBuilder()
            categoryString.append("Category: ")
            categoryString.append(item.category)
            binding.category.text = categoryString

            binding.seeMore.setOnClickListener {
                if (binding.group.visibility == View.GONE) {
                    binding.group.visibility = View.VISIBLE
                    binding.seeMore.text = it.context.getString(R.string.see_less)
                    binding.seeMore.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_baseline_keyboard_arrow_up_24,
                        0
                    )

                } else {
                    binding.group.visibility = View.GONE
                    binding.seeMore.text = it.context.getString(R.string.see_more)
                    binding.seeMore.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_baseline_keyboard_arrow_down_24,
                        0
                    )
                }
            }

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): QuizScoreListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemQuizBinding.inflate(layoutInflater, parent, false)
                return QuizScoreListViewHolder(binding)
            }
        }
    }
}

class QuizDiffCallback : DiffUtil.ItemCallback<Quiz>() {
    override fun areItemsTheSame(oldItem: Quiz, newItem: Quiz): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Quiz, newItem: Quiz): Boolean {
        return oldItem == newItem
    }
}
