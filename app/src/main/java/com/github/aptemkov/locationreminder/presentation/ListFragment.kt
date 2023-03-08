package com.github.aptemkov.locationreminder.presentation

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.aptemkov.locationreminder.R
import com.github.aptemkov.locationreminder.data.repository.TaskRepositoryImpl
import com.github.aptemkov.locationreminder.data.storage.FirebaseTaskStorage
import com.github.aptemkov.locationreminder.databinding.FragmentListBinding
import com.github.aptemkov.locationreminder.domain.usecases.GetTaskListUseCase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment() {

    private val viewModel: ListViewModel by activityViewModels()

    private val repository by lazy {
        com.github.aptemkov.locationreminder.data.repository.TaskRepositoryImpl(
            taskStorage = com.github.aptemkov.locationreminder.data.storage.FirebaseTaskStorage()
        )
    }
    private val useCase =
        com.github.aptemkov.locationreminder.domain.usecases.GetTaskListUseCase(repository)

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    //private val firebaseStore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
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
            layoutManager = LinearLayoutManager(this.context)
            this.adapter = adapter
        }

        /* BEFORE CLEAN ARCHITECTURE

        firebaseStore
            .collection("users").document(auth.currentUser!!.uid)
            .collection("tasks")
            //.orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
            if (value != null) {
                val tasks = value.toObjects(Task::class.java)
                adapter.submitList(tasks)
            } else {
                Toast.makeText(context, "${error?.message}", Toast.LENGTH_SHORT).show()
            }
        }
        AFTER CLEAN ARCHITECTURE
        */

//        val tasksList = useCase.execute()
//        tasksList.observe(viewLifecycleOwner) {
//            adapter.submitList(it)
//            Log.i("TEST", "$it")
//        }

        viewModel.tasksLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun setupMenu() {

        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_logout -> {
                        auth.signOut()
                        findNavController().popBackStack()
                        true
                    }
                    else -> { return true }
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}