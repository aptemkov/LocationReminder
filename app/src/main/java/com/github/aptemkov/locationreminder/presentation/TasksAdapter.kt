package com.github.aptemkov.locationreminder.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.aptemkov.locationreminder.R
import com.github.aptemkov.locationreminder.databinding.TaskItemBinding
import com.github.aptemkov.locationreminder.domain.models.Task


interface TasksActionListener {
    fun onTaskClicked(task: Task)
    fun onSwitchClicked(task: Task)
}

class TasksAdapter(
    private val actionListener: TasksActionListener
) : ListAdapter<Task, TasksAdapter.TaskViewHolder>(DiffCallback), View.OnClickListener {

    override fun onClick(v: View?) {
        val task = v?.tag as Task
        when (v.id) {
            R.id.is_active_switch -> {
                actionListener.onSwitchClicked(task)
            }
            else -> {
                actionListener.onTaskClicked(task)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {

        val binding = TaskItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        binding.root.setOnClickListener(this)
        binding.isActiveSwitch.setOnClickListener(this)

        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
        holder.binding.isActiveSwitch.tag = current
        holder.itemView.tag = current
    }

    class TaskViewHolder(val binding: TaskItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.apply {
                textViewTitle.text = task.title
                textViewDescription.text = task.description
                isActiveSwitch.isChecked = task.active
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