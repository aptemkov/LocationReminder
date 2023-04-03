package com.github.aptemkov.locationreminder.data.storage

import android.util.Log
import com.github.aptemkov.locationreminder.domain.models.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseTaskStorage @Inject constructor(
    private val auth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
) : TaskStorage {

    private var listener: ListenerRegistration? = null
    private var listenerService: ListenerRegistration? = null

    val taskCollectionRef =
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(auth.currentUser!!.uid)
            .collection("tasks")

    override fun startTasksListener(result: (List<Task>) -> Unit) {
        val firestore = FirebaseFirestore.getInstance()

        val collection = firestore
            .collection("users")
            .document(auth.currentUser!!.uid)
            .collection("tasks")
            .orderBy("createdAt", Query.Direction.DESCENDING)


        listener = collection.addSnapshotListener { value, error ->
            if (value != null) {
                val list = value.toObjects(Task::class.java)

                result(list)
                Log.i("STORAGE", "List: $list")
            }
        }
    }


    fun startTasksListenerFromService(result: (List<Task>) -> Unit) {
        val firestore = FirebaseFirestore.getInstance()

        val collection = firestore
            .collection("users")
            .document(auth.currentUser!!.uid)
            .collection("tasks")
            .orderBy("createdAt", Query.Direction.DESCENDING)


        listenerService = collection.addSnapshotListener { value, error ->
            if (value != null) {
                val list = value.toObjects(Task::class.java)

                result(list)
                Log.i("STORAGE", "List: $list")
            }
        }
    }

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
                Log.i("FIREBASE", "successfully added")
            }
            .addOnFailureListener {
                exception = it
                result = false
                Log.e("FIREBASE", "adding failure", it)
            }
        return Pair(result, exception)
    }

    override fun delete(task: Task) {
        TODO("Not yet implemented")
    }

    override fun edit(task: Task) {

        CoroutineScope(Dispatchers.IO).launch {

            val taskQuery = taskCollectionRef
                .whereEqualTo("taskId", task.taskId)
                .whereEqualTo("createdAt", task.createdAt)
                .get()
                .await()

            if (taskQuery.documents.isNotEmpty()) {
                for (document in taskQuery) {
                    try {
                        taskCollectionRef.document(document.id)
                            .update("active", !task.active)
                            .addOnSuccessListener {
                                Log.d("UPDATE", "DocumentSnapshot successfully updated!")
                            }
                            .addOnFailureListener {
                                Log.w("UPDATE", "Error updating document", it)
                            }
                    } catch (e: Exception) {
                        Log.d("UPDATE", "${e.message}")
                    }
                }
            }
        }

    }

    override fun get(position: Int): Task {
        TODO("Not yet implemented")
    }


}
