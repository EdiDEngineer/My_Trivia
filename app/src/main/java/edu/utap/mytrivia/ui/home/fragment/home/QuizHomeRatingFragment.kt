package edu.utap.mytrivia.ui.home.fragment.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import edu.utap.mytrivia.R
import edu.utap.mytrivia.databinding.FragmentQuizHomeRatingBinding
import edu.utap.mytrivia.ui.home.fragment.home.viewModel.QuizHomeViewModel
import edu.utap.mytrivia.util.showSnackBar

@AndroidEntryPoint
class QuizHomeRatingFragment : Fragment(R.layout.fragment_quiz_home_rating) {
    
    private lateinit var binding: FragmentQuizHomeRatingBinding
    val viewModel: QuizHomeViewModel by activityViewModels()
    private val navController by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentQuizHomeRatingBinding.bind(requireView())
        binding.fragment = this
        binding.rating.setOnRatingBarChangeListener { _, rating, _ ->
            viewModel.setRating(rating)
        }
    }

    fun save() {
        if (viewModel.saveValid()) {
            viewModel.save()
            navController.popBackStack()
        } else {
            showSnackBar("Rate quiz to finish.", Snackbar.LENGTH_SHORT)
        }
    }

}