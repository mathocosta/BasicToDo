package com.mathocosta.basictodo.activity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.mathocosta.basictodo.database.TaskDAO
import com.mathocosta.basictodo.datasource.TaskDataSource
import com.mathocosta.basictodo.model.Task

class AddTaskViewModel(application: Application) : AndroidViewModel(application) {
    private val taskDao: TaskDAO =
        TaskDataSource.getTaskDao(getApplication<Application>().applicationContext)

    var editingTaskId: Int? = null

    fun saveTask(task: Task) {
        if (editingTaskId == null) {
            taskDao.insertTask(task)
        } else {
            task.id = editingTaskId ?: 0
            taskDao.updateTask(task)
        }
    }

    fun getTaskById(taskId: Int): Task = taskDao.getTaskById(taskId)
}