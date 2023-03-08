package com.github.aptemkov.locationreminder.data.storage

import androidx.lifecycle.LiveData
import com.github.aptemkov.locationreminder.domain.models.Task

interface TaskStorage {

    fun add(task: Task): Pair<Boolean, Exception?>
    fun delete(task: Task)
    fun edit(task: Task)
    fun get(position: Int): Task
    fun getList(): LiveData<List<Task>>

}