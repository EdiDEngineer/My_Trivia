package edu.utap.mytrivia.ui.home.fragment.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import edu.utap.mytrivia.R
import edu.utap.mytrivia.databinding.FragmentQuizHomePlayBinding
import edu.utap.mytrivia.ui.home.fragment.home.adapter.QuizChoicesListAdapter
import edu.utap.mytrivia.ui.home.fragment.home.adapter.QuizChoicesListener
import edu.utap.mytrivia.ui.home.fragment.home.viewModel.QuizHomeViewModel
import edu.utap.mytrivia.util.fromHtmlToString
import edu.utap.mytrivia.util.showShortToast

class QuizHomePlayFragment : Fragment(R.layout.fragment_quiz_home_play) {

    private lateinit var binding: FragmentQuizHomePlayBinding
    private val navController by lazy {
        findNavController()
    }
    val viewModel: QuizHomeViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding = FragmentQuizHomePlayBinding.bind(requireView())
        binding.fragment = this
        binding.swipe.isRefreshing = true
        binding.lifecycleOwner = viewLifecycleOwner

        binding.recyclerView.adapter = QuizChoicesListAdapter(QuizChoicesListener {
            if (viewModel.setAnswer(it)) {
                navController.navigate(QuizHomePlayFragmentDirections.actionQuizHomePlayFragmentToQuizHomeRatingFragment())
            }
        })

        QuizTimer(lifecycle, { viewModel.startTimer() }, { viewModel.stopTimer() })

        viewModel.currentTime.observe(viewLifecycleOwner, {
            if (it == "00:00") {
                navController.navigate(QuizHomePlayFragmentDirections.actionQuizHomePlayFragmentToQuizHomeRatingFragment())
            }
        })

        binding.swipe.setOnRefreshListener {
            viewModel.stopTimer()
            viewModel.getQuestions()
        }

        viewModel.question.observe(viewLifecycleOwner, {
            if (it != null) {
                binding.swipe.isRefreshing = false
                binding.textQuestion.text = it.question.fromHtmlToString()
            }
        })

        viewModel.settingsInvalid.observe(viewLifecycleOwner,{
            if (it){
                activity?.showShortToast("No questions match your settings")
                navController.navigate(QuizHomePlayFragmentDirections.actionQuizHomePlayFragmentToQuizHomeSelectFragment())
            }
        })

    }
}