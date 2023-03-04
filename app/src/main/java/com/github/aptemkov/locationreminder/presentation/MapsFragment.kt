package com.github.aptemkov.locationreminder.presentation

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHost
import com.github.aptemkov.locationreminder.R
import com.github.aptemkov.locationreminder.databinding.FragmentMapsBinding
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar


class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    var marker: Marker? = null
    var circle: Circle? = null
    private var radius = 25.0

    private val callback = OnMapReadyCallback { googleMap ->

        googleMap.setOnCameraMoveListener {
            //binding.selectBtn.isClickable = true
            marker?.remove()
            circle?.remove()

            val cameraPosition = googleMap.cameraPosition.target

            marker =
                googleMap.addMarker(MarkerOptions()
                    .position(cameraPosition)
                    //.anchor(0.5f, .05f)
                    .title("Current location")
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

            Log.d("MAP", "Map Coordinate: ${marker?.position}")

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
        circle?.radius = 50.0
        binding.seekBar.value = 50.0f
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
                when(menuItem.itemId) {
                    R.id.action_done -> {
                        Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show() }

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