package edu.utap.mytrivia.ui.home.fragment.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.utap.mytrivia.R
import edu.utap.mytrivia.databinding.FragmentQuizHomeBinding


@AndroidEntryPoint
class QuizHomeFragment : Fragment(R.layout.fragment_quiz_home) {

    private val navController by lazy {
        findNavController()
    }
    private lateinit var binding: FragmentQuizHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentQuizHomeBinding.bind(requireView())
        binding.fragment = this
    }


    fun play() {
        navController.navigate(QuizHomeFragmentDirections.actionNavigationHomeToQuizHomeSelectFragment())
    }

}