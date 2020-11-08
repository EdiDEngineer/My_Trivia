package edu.utap.mytrivia.ui.home.fragment.dashboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import edu.utap.mytrivia.R
import edu.utap.mytrivia.databinding.FragmentQuizDashboardBinding

class QuizDashboardFragment : Fragment(R.layout.fragment_quiz_dashboard) {

    private val navController by lazy {
        findNavController()
    }
    private lateinit var binding: FragmentQuizDashboardBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentQuizDashboardBinding.bind(requireView())
        binding.fragment = this

    }

    fun anyDashboard() {
        navigateToDashboard("Any")
    }

    fun easyDashboard() {
        navigateToDashboard("Easy")
    }

    fun mediumDashboard() {
        navigateToDashboard("Medium")
    }

    fun hardDashboard() {
        navigateToDashboard("Hard")
    }

    private fun navigateToDashboard(difficulty: String) {
        navController.navigate(
            QuizDashboardFragmentDirections.actionNavigationQuizDashboardToQuizDashboardViewFragment(
                difficulty
            )
        )
    }

}