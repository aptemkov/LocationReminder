package com.github.aptemkov.locationreminder.data.storage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.aptemkov.locationreminder.domain.models.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class FirebaseTaskStorage : TaskStorage {

    //TODO(@Inject properties)
    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = Firebase.auth

    override fun add(task: Task): Pair<Boolean, Exception?> {
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

    override fun delete(task: Task) {
        TODO("Not yet implemented")
    }

    override fun edit(task: Task) {
        TODO("Not yet implemented")
    }

    override fun get(position: Int): Task {
        TODO("Not yet implemented")
    }

    override fun getList(): LiveData<List<Task>> {
        val tasksList = MutableLiveData<List<Task>>()
        firebaseFirestore
            .collection("users").document(auth.currentUser!!.uid)
            .collection("tasks")
            //.orderBy("createdAt", "desc")
            .addSnapshotListener { value, error ->
                if (value != null) {
                    tasksList.value = value.toObjects(Task::class.java)
                }
            }
        return tasksList
    }
}
