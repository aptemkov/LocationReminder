package com.github.aptemkov.locationreminder.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.github.aptemkov.locationreminder.Task
import com.github.aptemkov.locationreminder.databinding.FragmentAddingBinding
import com.google.firebase.firestore.FirebaseFirestore

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AddingFragment : Fragment() {

    private var _binding: FragmentAddingBinding? = null
    private lateinit var firebaseStore: FirebaseFirestore

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        firebaseStore = FirebaseFirestore.getInstance()
        _binding = FragmentAddingBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSaveUser.setOnClickListener {
            saveTask(
                /*name = binding.editTextName.text.toString(),
                lastName = binding.editTextLastName.text.toString(),
                age = binding.editTextAge.text.toString().toInt(),
                sex = "Мужской",*/
            )
        }
        binding
    }

    private fun saveTask(
        //name: String, lastName: String, age: Int, sex: String
    ) {
        //if (name.isNotBlank() && lastName.isNotBlank() && sex.isNotBlank()) {
            firebaseStore.collection("tasks").add(
                //Task(name, lastName, age, sex)
            Task()
            )
                .addOnSuccessListener {
                    findNavController().popBackStack()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error: " + it.message, Toast.LENGTH_SHORT).show();
                }
        //}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}