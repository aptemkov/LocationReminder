package com.github.aptemkov.locationreminder.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.aptemkov.locationreminder.MainActivity
import com.github.aptemkov.locationreminder.R
import com.github.aptemkov.locationreminder.data.repository.TaskRepositoryImpl
import com.github.aptemkov.locationreminder.data.storage.FirebaseTaskStorage
import com.github.aptemkov.locationreminder.databinding.FragmentListBinding
import com.github.aptemkov.locationreminder.domain.usecases.SubscribeToTaskListUseCase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment() {


    private val viewModel: ListViewModel by activityViewModels()

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val auth by lazy { Firebase.auth }

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
            adapter.submitList(it)
        }

        viewModel.isAuthorizated.observe(viewLifecycleOwner) {
            if(it == false) {
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
                    else -> {
                        return true
                    }
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}