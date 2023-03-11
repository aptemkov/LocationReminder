package com.github.aptemkov.locationreminder.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.aptemkov.locationreminder.databinding.TaskItemBinding
import com.github.aptemkov.locationreminder.domain.models.Task


class TasksAdapter(private val onTaskClicked: (Task) -> Unit) :
    ListAdapter<Task, TasksAdapter.TaskViewHolder>(DiffCallback) {

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

        fun bind(task: Task) {
            binding.apply {
                textViewTitle.text = task.title
                textViewDescription.text = task.description
                activitySwitch.isChecked = task.isActive
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldTask: Task, newTask: Task): Boolean {
                return oldTask === newTask
            }

            override fun areContentsTheSame(oldTask: Task, newTask: Task): Boolean {
                return oldTask.createdAt == newTask.createdAt
            }
        }
    }


}