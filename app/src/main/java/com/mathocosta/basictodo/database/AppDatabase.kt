package com.mathocosta.basictodo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mathocosta.basictodo.model.Task

@Database(entities = [Task::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDAO
}