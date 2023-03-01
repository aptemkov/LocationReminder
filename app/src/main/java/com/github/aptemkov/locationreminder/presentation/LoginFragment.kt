package com.github.aptemkov.locationreminder.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.aptemkov.locationreminder.R
import com.github.aptemkov.locationreminder.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null


    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        auth = Firebase.auth
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        println(currentUser)
        if (currentUser != null) {
            findNavController().navigate(R.id.action_loginFragment_to_ListFragment)
        }
        binding.loginBtn.setOnClickListener {
            signIn(
                binding.email.text.toString().trim(),
                binding.password.text.toString().trim(),
            )
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvChangeAuthWay.setOnCheckedChangeListener { buttonView, isChecked ->
            when (isChecked) {
                true -> {
                    binding.tvChangeAuthWay.text = getString(R.string.account_register)
                    binding.authWayTv.text = getString(R.string.log_in)
                    binding.loginBtn.setOnClickListener {
                        signIn(
                            binding.email.text.toString().trim(),
                            binding.password.text.toString().trim(),
                        )
                    }
                }
                false -> {

                    binding.tvChangeAuthWay.text = getString(R.string.account_log_in)
                    binding.authWayTv.text = getString(R.string.sign_up)
                    binding.loginBtn.setOnClickListener {
                        createAccount(
                            binding.email.text.toString().trim(),
                            binding.password.text.toString().trim(),
                        )
                    }
                }
            }
        }
    }


    private fun createAccount(email: String, password: String) {
        if (email.isNotBlank() && password.isNotBlank()) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        findNavController().navigate(R.id.action_loginFragment_to_ListFragment)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", it.exception)
                        Toast.makeText(context, "Authentication failed. ${it.exception}",
                            Toast.LENGTH_SHORT).show()

                    }
                }
        }
    }

    private fun signIn(email: String, password: String) {
        if (email.isNotBlank() && password.isNotBlank()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        findNavController().navigate(R.id.action_loginFragment_to_ListFragment)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", it.exception)
                        Toast.makeText(context, "Authentication failed. ${it.exception}",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }


    companion object {
        private const val TAG = "EmailPassword"
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}