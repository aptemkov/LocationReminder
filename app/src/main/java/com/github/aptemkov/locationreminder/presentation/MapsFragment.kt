package com.github.aptemkov.locationreminder.presentation

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.github.aptemkov.locationreminder.MapResponse
import com.github.aptemkov.locationreminder.R
import com.github.aptemkov.locationreminder.databinding.FragmentMapsBinding
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsFragment : Fragment() {

    private val viewModel: AddingReminderViewModel by activityViewModels()

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    var marker: Marker? = null
    var circle: Circle? = null
    private var radius: Double = 50.0

    private val callback = OnMapReadyCallback { googleMap ->

        val startPosition = LatLng(0.0, 0.0)
        marker =
            googleMap.addMarker(MarkerOptions()
                .position(startPosition)
                .title(getString(R.string.current_location))
            )

        circle = googleMap.addCircle(CircleOptions()
            .center(startPosition)
            .radius(radius)
        )


        googleMap.setOnCameraMoveListener {
            marker?.remove()
            circle?.remove()

            val cameraPosition = googleMap.cameraPosition.target

            marker =
                googleMap.addMarker(MarkerOptions()
                    .position(cameraPosition)
                    .title(getString(R.string.current_location))
                )

            circle = googleMap.addCircle(CircleOptions()
                .center(LatLng(cameraPosition.latitude, cameraPosition.longitude))
                .radius(radius)
                .strokeColor(ContextCompat.getColor(requireActivity().applicationContext,
                    R.color.map_stroke_color))
                .fillColor(ContextCompat.getColor(requireActivity().applicationContext,
                    R.color.map_main_color))
                .strokeWidth(0.5f)
            )

            Log.d("MAP", "Map position: ${marker?.position}")

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenu()

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        binding.seekBar.value = radius.toFloat()
        binding.selectBtn.setOnClickListener {
            Snackbar.make(
                binding.root,
                "${marker?.position} - radius ${circle?.radius}",
                Snackbar.LENGTH_SHORT)
                .show()
        }

        binding.seekBar.addOnChangeListener { slider, value, fromUser ->
            binding.selectBtn.text = value.toInt().toString()
            radius = value.toDouble()
            circle?.radius = value.toDouble()
        }

    }


    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_map, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.action_done -> {
                        val response =
                            MapResponse(
                                marker?.position ?: LatLng(0.0, 0.0),
                                circle?.radius ?: 50.0
                            )

                        viewModel.sendResponse(response)
                        findNavController().popBackStack()
                    }
                    android.R.id.home -> { findNavController().popBackStack() }

                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}