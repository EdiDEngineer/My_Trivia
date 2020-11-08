package edu.utap.mytrivia.ui.home.fragment.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.utap.mytrivia.databinding.ItemChoiceBinding


class QuizChoicesListAdapter(private val clickListener: QuizChoicesListener) : ListAdapter<String,
        QuizChoicesListAdapter.QuizChoicesListViewHolder>(QuizChoicesDiffCallback()) {

    override fun onBindViewHolder(holder: QuizChoicesListViewHolder, position: Int) {
        holder.bind(clickListener, getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizChoicesListViewHolder {
        return QuizChoicesListViewHolder.from(parent)
    }

    class QuizChoicesListViewHolder private constructor(val binding: ItemChoiceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: QuizChoicesListener, item: String) {
            binding.choice = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): QuizChoicesListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemChoiceBinding.inflate(layoutInflater, parent, false)
                return QuizChoicesListViewHolder(binding)
            }
        }
    }
}

class QuizChoicesListener(val clickListener: (choice: String) -> Unit) {
    fun onClick(choice: String) = clickListener(choice)
}

class QuizChoicesDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}