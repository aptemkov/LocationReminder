package com.github.aptemkov.locationreminder.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.aptemkov.locationreminder.domain.Task
import com.github.aptemkov.locationreminder.domain.TasksListRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class TaskListRepositoryImpl : TasksListRepository {
    //TODO(@Inject properties)
    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = Firebase.auth

    override fun addTask(task: Task): Pair<Boolean, Exception?> {
        var result: Boolean = true
        var exception: Exception? = null
        firebaseFirestore
            .collection("users")
            .document(auth.currentUser!!.uid)
            .collection("tasks")
            .add(task)
            .addOnSuccessListener {
                exception = null
            }
            .addOnFailureListener {
                exception = it
                result = false
            }
        return Pair(result, exception)
    }

    override fun deleteTask(task: Task) {
        TODO("Not yet implemented")
    }

    override fun editTask(task: Task) {
        TODO("Not yet implemented")
    }

    override fun getTask(position: Int): Task {
        TODO("Not yet implemented")
    }

    override fun getTasksList(): LiveData<List<Task>> {
        val tasksList = MutableLiveData<List<Task>>()
        firebaseFirestore
            .collection("users").document(auth.currentUser!!.uid)
            .collection("tasks")
            //.orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (value != null) {
                    tasksList.value = value.toObjects(Task::class.java)
                }
            }
        return tasksList

        TODO("Not yet implemented")
    }


}