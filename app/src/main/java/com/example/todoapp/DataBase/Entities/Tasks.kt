package com.example.todoapp.DataBase.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Tasks(
    @PrimaryKey(autoGenerate = true)
    var task_id: Int = 0,
    var task_category: String,
    var task_name: String,
    var timeData: String,
    var notificationTime: String,
    var mode: Boolean = false,
    var filePath:String = ""
):Serializable