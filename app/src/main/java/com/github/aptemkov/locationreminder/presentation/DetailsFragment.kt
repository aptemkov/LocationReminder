package com.github.aptemkov.locationreminder.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.github.aptemkov.locationreminder.databinding.FragmentDetailsBinding
import java.text.SimpleDateFormat

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    val args: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val task = args.task

        val simpleDateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm ")

        binding.apply {
            titleTv.text = task.title
            rangeTv.text = task.reminderRange.toInt().toString()
            locationTv.text = "Latitude: ${task.latitude}\nLongitude: ${task.longitude}"
            dateTv.text = simpleDateFormat.format(task.createdAt)
            descriptionTv.text = task.description
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}