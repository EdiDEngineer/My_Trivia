package edu.utap.mytrivia.ui.home.fragment.dashboard

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import edu.utap.mytrivia.R
import edu.utap.mytrivia.databinding.FragmentQuizDashboardViewBinding
import edu.utap.mytrivia.ui.home.HomeActivity
import edu.utap.mytrivia.ui.home.fragment.dashboard.adapter.QuizScoreListAdapter
import edu.utap.mytrivia.ui.home.fragment.dashboard.viewModel.QuizDashboardViewModel
import edu.utap.mytrivia.util.showDialog

class QuizDashboardViewFragment : Fragment(R.layout.fragment_quiz_dashboard_view) {
    private lateinit var binding: FragmentQuizDashboardViewBinding
    val viewModel: QuizDashboardViewModel by viewModels()
    private val navArgs: QuizDashboardViewFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding = FragmentQuizDashboardViewBinding.bind(requireView())
        viewModel.getQuizzesByDifficulty(navArgs.difficulty)
        binding.fragment = this
        binding.lifecycleOwner = viewLifecycleOwner
        (activity as HomeActivity).supportActionBar?.title =
            navArgs.difficulty + " Quizzes"

        val adapter = QuizScoreListAdapter()
        binding.searchEdit.editText?.doAfterTextChanged {
            it?.let {
                viewModel.search(it.toString())
                adapter.notifyDataSetChanged()
            }
        }

        binding.recyclerView.adapter = adapter

        initTouchHelper().attachToRecyclerView(binding.recyclerView)

        binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.multiChip.id -> viewModel.typeFilter("Multiple Choice")
                binding.boolChip.id -> viewModel.typeFilter("True / False")
                binding.anyChip.id -> viewModel.typeFilter("Any")
                else -> viewModel.typeFilter(null)
            }
        }

    }

    private fun initTouchHelper(): ItemTouchHelper {
        val simpleItemTouchCallback =
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
                ItemTouchHelper.START
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return true
                }

                override fun isLongPressDragEnabled(): Boolean {
                    return false
                }

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                    val pos = viewHolder.bindingAdapterPosition
                    val quiz =
                        viewModel.getQuizzesByDifficulty.value?.get(pos)
                    context?.showDialog(
                        message = "Are you sure you want to delete this quiz?",
                        negativeAction = { dialog, _ ->
                            dialog.dismiss()
                            binding.recyclerView.adapter?.notifyItemChanged(pos)
                        }
                    ) { _, _ ->
                        quiz?.let {
                            viewModel.deleteQuizById(it.id)
                        }
                    }
                }
            }
        return ItemTouchHelper(simpleItemTouchCallback)
    }

    fun deleteAll() {
        context?.showDialog(
            message = "Are you sure you want to delete quizzes for this difficulty?"
        ) { _, _ ->
            viewModel.deleteQuizzesByDifficulty(navArgs.difficulty)
        }
    }

}