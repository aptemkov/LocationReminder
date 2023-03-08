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
import com.github.aptemkov.locationreminder.databinding.FragmentAddingBinding
import com.github.aptemkov.locationreminder.domain.models.Task
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddingFragment : Fragment() {


    private var _binding: FragmentAddingBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: AddingReminderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddingBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewmodel.mapResponseLiveData.observe(viewLifecycleOwner) {
            if (it != null)
                binding.locationLabel.text = getString(R.string.location_selected)
        }

        binding.buttonSaveUser.setOnClickListener {

            val latitude = viewmodel.mapResponseLiveData.value?.latitude
            val longtitude = viewmodel.mapResponseLiveData.value?.longitude

            val position: LatLng? = if(
                    latitude != null && longtitude != null
            ) LatLng(latitude, longtitude)
            else null

            saveTask(
                title = binding.titleEditText.text.toString(),
                description = binding.descriptionEditText.text.toString(),
                position = position,
                radius = viewmodel.mapResponseLiveData.value?.radius,
            )
        }

        binding.locationLabel.setOnClickListener {
            findNavController().navigate(R.id.action_AddingFragment_to_mapsFragment)
        }

    }

    private fun saveTask(
        title: String, description: String, position: LatLng?, radius: Double?,
    ) {
        /*  BEFORE CLEAN ARCHITECTURE

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

            AFTER CLEAN ARCHITECTURE
        */

        if (title.isBlank() || description.isBlank()) {
            makeSnackBar(getString(R.string.all_fields_must_be_filled))
            return
        }

        if (position == null || radius == null) {
            makeSnackBar(getString(R.string.location_must_be_selected))
            return
        }

        val task = com.github.aptemkov.locationreminder.domain.models.Task(
            title = title,
            description = description,
            latitude = position.latitude,
            longitude = position.longitude,
            reminderRange = radius,
        )

        viewmodel.saveTask(task)

        viewmodel.saveResultLiveData.observe(viewLifecycleOwner) {
            if(it.first == true) {
                findNavController().popBackStack()
            } else {
                Toast.makeText(context, "Error: " + it.second?.message, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun makeSnackBar(text: String) {
        Snackbar.make(
            binding.root,
            text,
            Snackbar.LENGTH_SHORT)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}