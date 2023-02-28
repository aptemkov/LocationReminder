package com.github.aptemkov.firebasetest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.aptemkov.locationreminder.databinding.UserItemBinding


class UsersAdapter(private val onUserClicked: (User) -> Unit) :
    ListAdapter<User, UsersAdapter.UserViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            UserItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onUserClicked(current)
        }
        holder.bind(current)
    }

    class UserViewHolder(private var binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.apply {
                textViewName.text = user.name
                textViewLastName.text = user.lastName
                textViewAge.text = user.age.toString()
                textViewSex.text = user.sex
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldUser: User, newUser: User): Boolean {
                return oldUser === newUser
            }

            override fun areContentsTheSame(oldUser: User, newUser: User): Boolean {
                return oldUser.name == newUser.name
            }
        }
    }


}

/*
private class UsersAdapter : RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {
    private var users: List<User>

    init {
        users = ArrayList()
    }

    fun getUsers(): List<User> {
        return users
    }

    fun setUsers(users: List<User>) {
        this.users = users
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): UsersViewHolder {
        val view: View =
            LayoutInflater.from(viewGroup.context).inflate(R.layout., viewGroup, false)
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(usersViewHolder: UsersViewHolder, i: Int) {
        val (name, lastName, age, sex) = users[i]
        usersViewHolder.textViewName.text = name
        usersViewHolder.textViewLastName.text = lastName
        usersViewHolder.textViewAge.text = String.format("%s", age)
        usersViewHolder.textViewSex.text = sex
    }

    override fun getItemCount(): Int {
        return users.size
    }

    internal inner class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewName: TextView
        private val textViewLastName: TextView
        private val textViewAge: TextView
        private val textViewSex: TextView

        init {
            textViewName = itemView.findViewById(R.id.textViewName)
            textViewLastName = itemView.findViewById(R.id.textViewLastName)
            textViewAge = itemView.findViewById(R.id.textViewAge)
            textViewSex = itemView.findViewById(R.id.textViewSex)
        }
    }
}*/