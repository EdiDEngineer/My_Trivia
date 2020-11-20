package edu.utap.mytrivia.ui.main.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import edu.utap.mytrivia.R
import edu.utap.mytrivia.databinding.FragmentWelcomeBinding


@AndroidEntryPoint
class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    private lateinit var binding: FragmentWelcomeBinding
    private val navController by lazy {
        findNavController()
    }
    val args: WelcomeFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWelcomeBinding.bind(requireView())
        binding.fragment = this
    }

    fun goHome() {
        navController.navigate(WelcomeFragmentDirections.actionWelcomeFragmentToHomeActivity())
        requireActivity().finish()
    }

}