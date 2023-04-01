package com.github.aptemkov.locationreminder.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.VIBRATOR_MANAGER_SERVICE
import android.content.Context.VIBRATOR_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
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
import com.github.aptemkov.locationreminder.hasVibrationPermission
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment() {


    private val viewModel: ListViewModel by activityViewModels()

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()

        // TODO(TEST FEATURE)

        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            0
        )

        // TODO(TEST FEATURE)


        val adapter = TasksAdapter {
            Toast.makeText(context, "$it", Toast.LENGTH_SHORT).show()
        }

        binding.newTaskFab.setOnClickListener {
            findNavController().navigate(R.id.action_ListFragment_to_AddingFragment)
        }

        with(binding.recyclerView) {
            this.layoutManager = LinearLayoutManager(this.context)
            this.adapter = adapter
        }

        viewModel.tasksLiveData.observe(viewLifecycleOwner) {
            binding.listEmptyLayout.root.visibility =
                if (it.isEmpty()) {
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

    }

    private fun restartApp() {
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

                    // TODO(TEST FEATURE)

                    R.id.test_start -> {
                        Intent(requireContext(), LocationService::class.java).apply {
                            action = LocationService.ACTION_START
                            requireActivity().startService(this)
                        }

                        //vibrate()
// TODO(return back)
                        return true
                    }

                    R.id.test_stop -> {
                        Intent(requireContext(), LocationService::class.java).apply {
                            action = LocationService.ACTION_STOP
                            requireActivity().startService(this)
                        }
                        return true
                    }

                    // TODO(TEST FEATURE)

                    else -> {
                        return true
                    }
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    @SuppressLint("MissingPermission")
    private fun vibrate() {

        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.VIBRATE
            ),
            0
        )

        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                requireContext().getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            requireContext().getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        Log.i("VIBRATOR", "has vibrator = ${vibrator.hasVibrator()}")
        if (vibrator.hasVibrator()) { // Vibrator availability checking
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)) // New vibrate method for API Level 26 or higher
                Log.i("VIBRATOR", "vibrated on sdk >= 26")
            } else {
                Log.i("VIBRATOR", "vibrated on sdk < 26")
                vibrator.vibrate(500) // Vibrate method for below API Level 26
            }
        }
        else Log.i("VIBRATOR", "failed")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}