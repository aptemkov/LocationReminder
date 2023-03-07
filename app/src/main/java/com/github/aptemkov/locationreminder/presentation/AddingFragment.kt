package com.github.aptemkov.locationreminder.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.aptemkov.locationreminder.R
import com.github.aptemkov.locationreminder.Task
import com.github.aptemkov.locationreminder.databinding.FragmentAddingBinding
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddingFragment : Fragment() {

    private var _binding: FragmentAddingBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: AddingReminderViewModel by activityViewModels()
    private lateinit var firebaseStore: FirebaseFirestore
    private val auth: FirebaseAuth by lazy { Firebase.auth }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        firebaseStore = FirebaseFirestore.getInstance()
        _binding = FragmentAddingBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewmodel.mapResponse.observe(viewLifecycleOwner) {
            binding.textTest.text = it.toString()
        }

        binding.buttonSaveUser.setOnClickListener {
            saveTask(
                title = binding.titleEditText.text.toString(),
                description = binding.descriptionEditText.text.toString(),
                position = viewmodel.mapResponse.value?.position ?: LatLng(0.0, 0.0),
                radius = viewmodel.mapResponse.value?.radius ?: 0.0,
            )
        }

        binding.locationLabel.setOnClickListener {
            findNavController().navigate(R.id.action_AddingFragment_to_mapsFragment)
        }

    }

    private fun saveTask(
        title: String, description: String, position: LatLng, radius: Double,
    ) {
        if (title.isNotBlank() && description.isNotBlank()) {
            firebaseStore
                .collection("users").document(auth.currentUser!!.uid)
                .collection("tasks")
                .add(
                    Task(
                        title = title,
                        description = description,
                        latitude = position.latitude,
                        longitude = position.longitude,
                        reminderRange = radius,
                    )
                )
                .addOnSuccessListener {
                    findNavController().popBackStack()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error: " + it.message, Toast.LENGTH_SHORT).show();
                }
        } else {
            Snackbar.make(
                binding.root,
                "All field must be filled",
                Snackbar.LENGTH_SHORT)
                .show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}