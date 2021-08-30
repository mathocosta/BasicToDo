package com.mathocosta.basictodo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    val title: String,
    @ColumnInfo(name = "date_str") val date: String,
    @ColumnInfo(name = "time_str") val time: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
