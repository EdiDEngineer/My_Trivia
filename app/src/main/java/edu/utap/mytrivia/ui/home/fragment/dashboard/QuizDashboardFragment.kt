package edu.utap.mytrivia.ui.home.fragment.dashboard

import android.animation.Animator
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import dagger.hilt.android.AndroidEntryPoint
import edu.utap.mytrivia.R
import edu.utap.mytrivia.databinding.FragmentQuizDashboardBinding
import edu.utap.mytrivia.ui.home.fragment.dashboard.viewModel.QuizDashboardViewModel
import kotlin.math.hypot

@AndroidEntryPoint
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
        binding.lifecycleOwner = viewLifecycleOwner
        createReveal(binding.anyDifficultyGroup)
        createReveal(binding.easyDifficultyGroup)
        createReveal(binding.mediumDifficultyGroup)
        createReveal(binding.hardDifficultyGroup)
        viewModel.resetQuizzes()
    }


    private fun createReveal(
        view: View
    ): Animator {
        val finalRadius = hypot((view.width / 2).toDouble(), (view.height / 2).toDouble()).toFloat()

        return ViewAnimationUtils.createCircularReveal(
            view,
            (view.width / 2),
            (view.height / 2),
            0f,
            finalRadius
        ).apply {
            start()
        }
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
        viewModel.getQuizzesByDifficulty(difficulty)
        navController.navigate(
            QuizDashboardFragmentDirections.actionNavigationQuizDashboardToQuizDashboardViewFragment(
                difficulty
            )
        )
    }

}