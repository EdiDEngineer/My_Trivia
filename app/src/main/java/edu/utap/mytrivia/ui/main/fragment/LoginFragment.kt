package edu.utap.mytrivia.ui.main.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import edu.utap.mytrivia.R
import edu.utap.mytrivia.databinding.FragmentLoginBinding
import edu.utap.mytrivia.ui.main.viewModel.MainViewModel
import edu.utap.mytrivia.util.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    val viewModel: MainViewModel by activityViewModels()
    private val navController by lazy {
        findNavController()
    }
    private fun isValidEmail() =
        binding.loginEmail.editText?.text?.toString()?.isValidEmail() ?: false

    private fun isValidPass() =
        binding.loginPass.editText?.text?.toString()?.isValidPassword() ?: false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

    }


    fun login() {
        if (!isValidEmail()) {
            showSnackBar("Enter valid email", Snackbar.LENGTH_SHORT)
        } else if (!isValidPass()) {
            showSnackBar("Enter valid password", Snackbar.LENGTH_SHORT)
        } else {
            viewModel.load()
            lifecycleScope.launch {
                try {
                    viewModel.firebaseUserAuthLiveData.signIn(
                        binding.loginEmail.editText?.text.toString(),
                        binding.loginPass.editText?.text.toString()
                    ).await()


                } catch (e: FirebaseNetworkException) {
                    viewModel.resetLoad()
                    showSnackBar(
                        getString(R.string.network_error),
                        Snackbar.LENGTH_SHORT
                    )
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    viewModel.resetLoad()
                    showSnackBar(
                        "Username and password do not match",
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


    fun signUp() {
        navController.navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
    }

    private fun initViews() {
        binding = FragmentLoginBinding.bind(requireView())
        binding.fragment = this
        binding.lifecycleOwner = viewLifecycleOwner
        hideKeyboard()
        binding.loginEmail.validate(
            "Invalid email address",
        ) {
            it.isValidEmail()
        }

        binding.loginPass.validate(
            "Password must be greater than 5 characters",
        ) {
            it.isValidPassword()
        }

        viewModel.firebaseUserAuthLiveData.observe(viewLifecycleOwner, {
            it?.email?.let {
                navController.navigate(
                    LoginFragmentDirections.actionLoginFragmentToWelcomeFragment(
                        it
                    )
                )
            }
        })

    }
}