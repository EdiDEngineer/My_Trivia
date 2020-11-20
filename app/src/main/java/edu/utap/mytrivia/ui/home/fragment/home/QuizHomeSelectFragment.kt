package edu.utap.mytrivia.ui.home.fragment.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import edu.utap.mytrivia.R
import edu.utap.mytrivia.databinding.FragmentQuizHomeSelectBinding
import edu.utap.mytrivia.ui.home.fragment.home.viewModel.QuizHomeViewModel
import edu.utap.mytrivia.util.minMaxInputFilter
import edu.utap.mytrivia.util.showSnackBar

@AndroidEntryPoint
class QuizHomeSelectFragment : Fragment(R.layout.fragment_quiz_home_select) {
    private lateinit var binding: FragmentQuizHomeSelectBinding
    private val viewModel: QuizHomeViewModel by activityViewModels()
    private val navController by lazy {
        findNavController()
    }

    private fun isValid() = binding.noSelect.editText?.text?.toString()?.let {
        try {
            it.toInt() >= 5
        } catch (e: NumberFormatException) {
            false
        }
    } ?: false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentQuizHomeSelectBinding.bind(requireView())
        binding.fragment = this
        context?.let {
            binding.noSelect.editText?.filters = arrayOf(it.minMaxInputFilter(1, 50))
        }
        viewModel.resetQuiz()
    }

    fun start() {
        if (isValid()) {
            binding.noSelect.editText?.text?.let {
                viewModel.setQuiz(
                    it.toString(),
                    when (binding.difficultySpinner.selectedItemPosition) {
                        0 -> "Any"
                        else -> (binding.difficultySpinner.selectedItem as String)
                    },
                    when (binding.typeSpinner.selectedItemPosition) {
                        0 -> "Any"
                        else -> (binding.typeSpinner.selectedItem as String)
                    },
                    when (binding.categorySpinner.selectedItemPosition) {
                        0 -> "Any"
                        else -> (binding.categorySpinner.selectedItem as String)
                    }
                )
                navController.navigate(QuizHomeSelectFragmentDirections.actionQuizHomeSelectFragmentToQuizHomePlayFragment())
            }
        } else {
            showSnackBar("Enter valid number of questions.", Snackbar.LENGTH_SHORT)
        }
    }

}