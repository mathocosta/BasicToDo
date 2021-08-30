package com.mathocosta.basictodo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mathocosta.basictodo.R
import com.mathocosta.basictodo.databinding.ListItemTaskBinding
import com.mathocosta.basictodo.model.Task

class TaskListAdapter(private val listener: MenuActionClickListener) :
    ListAdapter<Task, TaskListAdapter.TaskViewHolder>(TaskListAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder =
        TaskViewHolder(
            ListItemTaskBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) =
        holder.bind(getItem(position), listener)

    class TaskViewHolder(private val binding: ListItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Task, clickListener: MenuActionClickListener) = with(binding) {
            listItemTvTitle.text = item.title
            val dateText = "${item.date} ${item.time}"
            listItemTvDate.text = dateText
            listItemIvMore.setOnClickListener {
                showPopup(item, clickListener)
            }
        }

        private fun showPopup(item: Task, clickListener: MenuActionClickListener) {
            with(binding.listItemIvMore) {
                val popupMenu = PopupMenu(context, this)
                popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.action_edit -> clickListener.onMenuEditClick(item)
                        R.id.action_delete -> clickListener.onMenuDeleteClick(item)
                    }

                    return@setOnMenuItemClickListener true
                }
                popupMenu.show()
            }
        }
    }

    companion object : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem == newItem

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean =
            oldItem.id == newItem.id
    }

    interface MenuActionClickListener {
        fun onMenuEditClick(item: Task)
        fun onMenuDeleteClick(item: Task)
    }
}