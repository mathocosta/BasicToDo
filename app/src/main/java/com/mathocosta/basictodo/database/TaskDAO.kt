package com.mathocosta.basictodo.database

import androidx.room.*
import com.mathocosta.basictodo.model.Task

@Dao
interface TaskDAO {
    @Query("SELECT * FROM task")
    fun getAllTasks(): List<Task>

    @Query("SELECT * FROM task WHERE id == :taskId")
    fun getTaskById(taskId: Int): Task

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Delete
    fun deleteTask(task: Task)
}