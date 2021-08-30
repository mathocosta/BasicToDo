package com.mathocosta.basictodo.datasource

import android.content.Context
import androidx.room.Room
import com.mathocosta.basictodo.database.AppDatabase
import com.mathocosta.basictodo.database.TaskDAO

object TaskDataSource {
    private var _sharedDb: AppDatabase? = null

    private fun getSharedDb(context: Context): AppDatabase = _sharedDb ?: run {
        Room.databaseBuilder(
            context, AppDatabase::class.java, "task-database"
        ).allowMainThreadQueries().build().also {
            _sharedDb = it
        }
    }

    fun getTaskDao(context: Context): TaskDAO = getSharedDb(context).taskDao()
}