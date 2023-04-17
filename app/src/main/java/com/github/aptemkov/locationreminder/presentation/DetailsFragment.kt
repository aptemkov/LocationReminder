package com.github.aptemkov.locationreminder.presentation

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.aptemkov.locationreminder.R
import com.github.aptemkov.locationreminder.databinding.FragmentDetailsBinding
import com.github.aptemkov.locationreminder.domain.models.MapResponse
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private val viewModel: DetailsViewModel by viewModels()
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    val args: DetailsFragmentArgs by navArgs()
    val task by lazy { args.task }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()

        val mapFragment = childFragmentManager.findFragmentById(R.id.smallMap) as SupportMapFragment?
        mapFragment?.getMapAsync(callBack)

        val simpleDateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm ")

        binding.apply {
            titleTv.text = task.title
            rangeTv.text = task.reminderRange.toInt().toString()
            locationTv.text = "Latitude: ${task.latitude}\nLongitude: ${task.longitude}"
            dateTv.text = simpleDateFormat.format(task.createdAt)
            descriptionTv.text = task.description
        }
    }

    val callBack = OnMapReadyCallback { googleMap ->

        val taskPosition = LatLng(task.latitude, task.longitude)

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(taskPosition, 13f))
        googleMap.uiSettings.isScrollGesturesEnabled = false

        googleMap.addMarker(MarkerOptions().position(taskPosition)
            .title(getString(R.string.current_location)))

        googleMap.addCircle(CircleOptions().center(taskPosition)
            .radius(task.reminderRange)
            .strokeColor(ContextCompat.getColor(requireActivity().applicationContext, R.color.map_stroke_color))
            .fillColor(ContextCompat.getColor(requireActivity().applicationContext, R.color.map_main_color))
            .strokeWidth(0.5f))
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_details, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.action_delete -> {
                        showConfirmationDialog()
                    }
                    android.R.id.home -> {
                        findNavController().popBackStack()
                    }
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext()).setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.are_you_sure_you_want_to_delete_task))
            .setCancelable(false)
            .setNegativeButton("No") { _, _ -> }
            .setPositiveButton("Yes") { _, _ ->
                deleteItem()
            }.show()
    }

    private fun deleteItem() {
        viewModel.delete(task)
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}