package edu.utap.mytrivia.ui.home.fragment.dashboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import edu.utap.mytrivia.R
import edu.utap.mytrivia.databinding.FragmentQuizDashboardBinding
import edu.utap.mytrivia.ui.home.fragment.dashboard.viewModel.QuizDashboardViewModel


class QuizDashboardFragment : Fragment(R.layout.fragment_quiz_dashboard) {

    val viewModel: QuizDashboardViewModel by activityViewModels()
    private val navController by lazy {
        findNavController()
    }
    private lateinit var binding: FragmentQuizDashboardBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentQuizDashboardBinding.bind(requireView())
        binding.fragment = this
        binding.lifecycleOwner= viewLifecycleOwner

    }

    fun sync() {
        viewModel.uploadQuizzes()
        animate()
    }

    private fun animate() {
        val drawable = context?.let {
            AnimatedVectorDrawableCompat.create(
                it,
                R.drawable.avd_sync_to_sync_done
            )
        }
        binding.fab.setImageDrawable(drawable)
        drawable?.start()
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