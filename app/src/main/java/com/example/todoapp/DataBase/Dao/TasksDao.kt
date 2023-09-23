package com.example.todoapp.DataBase.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.todoapp.DataBase.Entities.Tasks

@Dao
interface TasksDao {
    @Insert
    fun addTask(tasks: Tasks)

    @Update
    fun updateTask(tasks: Tasks)

    @Delete
    fun deleteTask(tasks: Tasks)

    @Query("select * from Tasks where task_category = :taskToifasi")
    fun getTasksByCategory(taskToifasi: String):List<Tasks>

    @Query("select * from Tasks")
    fun getAllTasks(): List<Tasks>

    @Query("select * from Tasks where mode = :berilgan")
    fun getTasksByMode(berilgan: Boolean): List<Tasks>

}