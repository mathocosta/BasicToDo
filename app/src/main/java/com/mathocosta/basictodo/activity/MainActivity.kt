package com.mathocosta.basictodo.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.mathocosta.basictodo.adapter.TaskListAdapter
import com.mathocosta.basictodo.databinding.ActivityMainBinding
import com.mathocosta.basictodo.model.Task

class MainActivity : AppCompatActivity(), TaskListAdapter.MenuActionClickListener {
    private val adapter by lazy { TaskListAdapter(this) }
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var viewModel: MainViewModel

    private val register =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.refreshScreen()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(MainViewModel::class.java)

        binding.rvTasks.adapter = adapter

        setClickListeners()
        setObservers()

        viewModel.refreshScreen()
    }

    private fun setClickListeners() = with(binding) {
        fabAdd.setOnClickListener {
            register.launch(Intent(this@MainActivity, AddTaskActivity::class.java))
        }
    }

    private fun setObservers() {
        viewModel.getTasks().observe(this) { tasks ->
            binding.layoutEmpty.root.visibility = if (tasks.isEmpty()) View.VISIBLE else View.GONE
            adapter.submitList(tasks)
        }
    }

    override fun onMenuEditClick(item: Task) {
        Intent(this, AddTaskActivity::class.java).apply {
            putExtra(AddTaskActivity.ADD_TASK_ID, item.id)
            register.launch(this)
        }
    }

    override fun onMenuDeleteClick(item: Task) {
        viewModel.deleteTask(item)
    }
}