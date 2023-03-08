package com.github.aptemkov.locationreminder.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.aptemkov.locationreminder.domain.models.Task
import com.github.aptemkov.locationreminder.databinding.TaskItemBinding


class TasksAdapter(private val onTaskClicked: (com.github.aptemkov.locationreminder.domain.models.Task) -> Unit) :
    ListAdapter<com.github.aptemkov.locationreminder.domain.models.Task, TasksAdapter.TaskViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            TaskItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onTaskClicked(current)
        }
        holder.bind(current)
    }

    class TaskViewHolder(private var binding: TaskItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: com.github.aptemkov.locationreminder.domain.models.Task) {
            binding.apply {
                textViewTitle.text = task.title
                textViewDescription.text = task.description
                activitySwitch.isChecked = task.isActive
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<com.github.aptemkov.locationreminder.domain.models.Task>() {
            override fun areItemsTheSame(oldTask: com.github.aptemkov.locationreminder.domain.models.Task, newTask: com.github.aptemkov.locationreminder.domain.models.Task): Boolean {
                return oldTask === newTask
            }

            override fun areContentsTheSame(oldTask: com.github.aptemkov.locationreminder.domain.models.Task, newTask: com.github.aptemkov.locationreminder.domain.models.Task): Boolean {
                return oldTask.createdAt == newTask.createdAt
            }
        }
    }


}