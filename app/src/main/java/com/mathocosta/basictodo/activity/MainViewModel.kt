package com.mathocosta.basictodo.activity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mathocosta.basictodo.database.AppDatabase
import com.mathocosta.basictodo.database.TaskDAO
import com.mathocosta.basictodo.datasource.TaskDataSource
import com.mathocosta.basictodo.model.Task

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val taskDao: TaskDAO =
        TaskDataSource.getTaskDao(getApplication<Application>().applicationContext)

    private val _tasksLiveData = MutableLiveData<List<Task>>()

    fun getTasks(): LiveData<List<Task>> = _tasksLiveData

    fun refreshScreen() {
        val allTasks = taskDao.getAllTasks()
        _tasksLiveData.value = allTasks
    }

    fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
        refreshScreen()
    }
}
