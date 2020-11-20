package edu.utap.mytrivia.ui.main.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import dagger.hilt.android.AndroidEntryPoint
import edu.utap.mytrivia.R
import edu.utap.mytrivia.databinding.FragmentSignupBinding
import edu.utap.mytrivia.ui.main.viewModel.MainViewModel
import edu.utap.mytrivia.util.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_signup) {
    private lateinit var binding: FragmentSignupBinding
    val viewModel: MainViewModel by activityViewModels()
    private val navController by lazy {
        findNavController()
    }

    private fun isValidEmail() =
        binding.signUpEmail.editText?.text?.toString()?.isValidEmail() ?: false

    private fun isValidPass() =
        binding.signUpPass.editText?.text?.toString()?.isValidPassword() ?: false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

    }


    fun signUp() {
        if (!isValidEmail()) {
            showSnackBar("Enter valid email", Snackbar.LENGTH_SHORT)
        } else if (!isValidPass()) {
            showSnackBar("Enter valid password", Snackbar.LENGTH_SHORT)
        } else {
            viewModel.load()
            lifecycleScope.launch {
                try {
                    viewModel.firebaseUserAuthLiveData.createAccount(
                        binding.signUpEmail.editText?.text.toString(),
                        binding.signUpPass.editText?.text.toString()
                    ).await()
                } catch (e: FirebaseNetworkException) {
                    viewModel.resetLoad()
                    showSnackBar(
                        getString(R.string.network_error),
                        Snackbar.LENGTH_SHORT
                    )
                } catch (e: Exception) {
                    viewModel.resetLoad()
                    showSnackBar(
                        getString(R.string.account_failed),
                        Snackbar.LENGTH_SHORT
                    )
                }
            }
        }
    }


    private fun initViews() {
        binding = FragmentSignupBinding.bind(requireView())
        binding.fragment = this
        binding.lifecycleOwner = viewLifecycleOwner
        hideKeyboard()
        binding.signUpEmail.validate(
            "Invalid email address",
        ) {
            it.isValidEmail()
        }

        binding.signUpPass.validate(
            "Password must be greater than 5 characters",
        ) {
            it.isValidPassword()
        }
        viewModel.firebaseUserAuthLiveData.observe(viewLifecycleOwner, {
            it?.email?.let {
                navController.navigate(
                    SignUpFragmentDirections.toWelcomeFragment(
                        it
                    )
                )
            }
        })


    }

    fun login() {
        navController.navigate(
            SignUpFragmentDirections.actionSignUpFragmentToLoginFragment(
            )
        )
    }

}