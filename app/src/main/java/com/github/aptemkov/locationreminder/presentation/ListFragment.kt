package com.github.aptemkov.locationreminder.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.aptemkov.locationreminder.Task
import com.github.aptemkov.locationreminder.TasksAdapter
import com.github.aptemkov.locationreminder.R
import com.github.aptemkov.locationreminder.databinding.FragmentListBinding
import com.google.firebase.firestore.FirebaseFirestore

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseStore: FirebaseFirestore
    private var adapter: TasksAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        firebaseStore = FirebaseFirestore.getInstance()
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TasksAdapter {
            Toast.makeText(context, "$it", Toast.LENGTH_SHORT).show()
        }

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_ListFragment_to_AddingFragment)
        }

        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(this.context)
            this.adapter = adapter
        }

        firebaseStore.collection("tasks").addSnapshotListener { value, error ->
            if (value != null) {
                val tasks = value.toObjects(Task::class.java)
                adapter.submitList(tasks)
                Toast.makeText(context, "${tasks.size}", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(context, "${error?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}