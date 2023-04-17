package com.github.aptemkov.locationreminder.presentation

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.aptemkov.locationreminder.LocationService
import com.github.aptemkov.locationreminder.MainActivity
import com.github.aptemkov.locationreminder.R
import com.github.aptemkov.locationreminder.databinding.FragmentListBinding
import com.github.aptemkov.locationreminder.domain.models.Task
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import hilt_aggregated_deps._com_github_aptemkov_locationreminder_presentation_ListFragment_GeneratedInjector

@AndroidEntryPoint
class ListFragment : Fragment() {


    private val viewModel: ListViewModel by activityViewModels()

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private var isLocationTrackingEnabled = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.actionBar?.setDisplayShowHomeEnabled(false)
        activity?.actionBar?.setDisplayHomeAsUpEnabled(false)

    startLocationTracking()
        setupMenu()
        requestDeviceLocationSettings()

        ActivityCompat.requestPermissions(requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            0)

        val adapter = TasksAdapter(object : TasksActionListener {
            override fun onTaskClicked(task: Task) {
                openDetailsFragment(task)
            }

            override fun onSwitchClicked(task: Task) {
                editTask(task)
            }
        })
        with(binding.recyclerView) {
            this.layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }
        initViews()

        viewModel.tasksLiveData.observe(viewLifecycleOwner) {
            binding.listEmptyLayout.root.visibility = if (it.isEmpty()) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
            adapter.submitList(it)
        }

        viewModel.isAuthorizated.observe(viewLifecycleOwner) {
            if (it == false) {
                restartApp()
            }
        }
        ///////


        ///////
    }

    private fun initViews() {
        binding.newTaskFab.setOnClickListener {
            findNavController().navigate(R.id.action_ListFragment_to_AddingFragment)
        }
    }

    private fun openDetailsFragment(task: Task) {
        val action = ListFragmentDirections.actionListFragmentToDetailsFragment(task)
        findNavController().navigate(action)
    }

    private fun editTask(task: Task) {
        viewModel.update(task)
    }

    private fun restartApp() {
        stopLocationTracking()
        val intent = Intent(requireContext(), MainActivity::class.java)
        requireActivity().finish()
        startActivity(intent)
    }

    private fun setupMenu() {

        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_logout -> {
                        viewModel.signOut()
                        findNavController().popBackStack()
                        true
                    }

                    R.id.test_stop -> {
                        switchMenuButtonStartStopTracking(menuItem)
                        return true
                    }

                    else -> {
                        return true
                    }
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    private fun switchMenuButtonStartStopTracking(menuItem: MenuItem) {


        if (isLocationTrackingEnabled) {
            isLocationTrackingEnabled = false
            stopLocationTracking()
            menuItem.title = "Start"
        } else {
            isLocationTrackingEnabled = true
            startLocationTracking()
            menuItem.title = "Stop"
        }
    }

    private fun stopLocationTracking() {
        Intent(requireContext(), LocationService::class.java).apply {
            action = LocationService.ACTION_STOP
            requireActivity().startService(this)
        }
    }

    private fun startLocationTracking() {
        Intent(requireContext(), LocationService::class.java).apply {
            action = LocationService.ACTION_START
            requireActivity().startService(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun requestDeviceLocationSettings() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(requireActivity())
        val task = client.checkLocationSettings(builder.build())

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(requireActivity(), 100)
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                }

            }
        }

    }
}