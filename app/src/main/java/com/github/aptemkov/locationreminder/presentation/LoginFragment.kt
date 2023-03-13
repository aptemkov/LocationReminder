package com.github.aptemkov.locationreminder.presentation

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.aptemkov.locationreminder.R
import com.github.aptemkov.locationreminder.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        auth = Firebase.auth
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = auth.currentUser
        println(currentUser)
        if (currentUser != null) {
            findNavController().navigate(R.id.action_loginFragment_to_ListFragment)
        }
        setLoginScreen()

        viewModel.errorLiveData.observe(viewLifecycleOwner) {

            when(it.first) {
                true -> {
                    findNavController().navigate(R.id.action_loginFragment_to_ListFragment)
                }
                false -> {
                    it.second?.message?.let { exceptionMessage ->
                        makeSnackBar(exceptionMessage)
                    }
                }
            }
        }

        binding.tvChangeAuthWay.setOnCheckedChangeListener { buttonView, isChecked ->
            when (isChecked) {
                true -> {
                    setLoginScreen()
                }
                false -> {
                    setRegisterScreen()
                }
            }
        }
    }

    private fun setLoginScreen() {
        with(binding) {
            tvChangeAuthWay.text = getString(R.string.account_register)
            authWayTv.text = getString(R.string.log_in)
            loginBtn.setOnClickListener {
                    signIn(
                        email.text.toString().trim(),
                        password.text.toString().trim(),
                    )
                hideKeyboard()
            }
        }


    }

    private fun setRegisterScreen() {
        with(binding) {
            tvChangeAuthWay.text = getString(R.string.account_log_in)
            authWayTv.text = getString(R.string.sign_up)
            loginBtn.setOnClickListener {
                createAccount(
                    email.text.toString().trim(),
                    password.text.toString().trim(),
                )
                hideKeyboard()
            }
        }
    }


    private fun createAccount(email: String, password: String) {
        if (email.isNotBlank() && password.isNotBlank()) {
            viewModel.createAccount(email, password)
        }
    }

    private fun signIn(email: String, password: String) {
        if (email.isNotBlank() && password.isNotBlank()) {
            viewModel.signIn(email, password)
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
    }

    private fun makeSnackBar(text: String) {
        Snackbar.make(
            binding.root,
            text,
            Snackbar.LENGTH_LONG)
            .show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}