package com.github.aptemkov.locationreminder.presentation

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.github.aptemkov.locationreminder.R
import com.github.aptemkov.locationreminder.databinding.FragmentMapsBinding
import com.github.aptemkov.locationreminder.domain.models.MapResponse
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.CancellationToken
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsFragment : Fragment() {

    private val viewModel: AddingReminderViewModel by activityViewModels()
    private val fusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private var marker: Marker? = null
    private var circle: Circle? = null
    private var radius: Double = 50.0

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->

        googleMap.isMyLocationEnabled = true

        getDeviceLocation(googleMap)

        val startPosition = LatLng(0.0, 0.0)
        marker = googleMap.addMarker(MarkerOptions().position(startPosition)
            .title(getString(R.string.current_location)))

        circle = googleMap.addCircle(CircleOptions().center(startPosition)
            .radius(radius)
            .strokeColor(ContextCompat.getColor(requireActivity().applicationContext, R.color.map_stroke_color))
            .fillColor(ContextCompat.getColor(requireActivity().applicationContext, R.color.map_main_color))
            .strokeWidth(0.5f))

        googleMap.setOnCameraMoveListener {

            val cameraPosition = googleMap.cameraPosition.target

            marker?.position = cameraPosition
            circle?.center = cameraPosition

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
        getLocationPermission()

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        binding.seekBar.value = radius.toFloat()
        binding.radiusTextView.text = radius.toInt().toString()

        binding.seekBar.addOnChangeListener { slider, value, fromUser ->
            binding.radiusTextView.text = value.toInt().toString()
            radius = value.toDouble()
            circle?.radius = value.toDouble()
        }

    }

    @SuppressLint("MissingPermission")
    private fun getLocationPermission() {
        if (!locationPermissionsAreGranted()) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                100)
            return
        }
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation(googleMap: GoogleMap) {

        try {
            if (locationPermissionsAreGranted()) {
                fusedLocationProviderClient.lastLocation
                    .addOnSuccessListener { location ->

                        Log.w("MAPS", "Task successful $location")

                        if (location != null) {
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                LatLng(location.latitude,
                                    location.longitude), 15f))
                        }

                    }
            }
        } catch (e: Exception) {
            Log.e("Exception: ", e.message, e)
        }
    }

    private fun locationPermissionsAreGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(requireContext(),
            android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }


    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_map, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.action_done -> {
                        val response = MapResponse(latitude = marker?.position?.latitude ?: 0.0,
                            longitude = marker?.position?.longitude ?: 0.0,
                            circle?.radius ?: 50.0)

                        viewModel.saveLocation(response)
                        findNavController().popBackStack()
                    }
                    android.R.id.home -> {
                        findNavController().popBackStack()
                    }

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