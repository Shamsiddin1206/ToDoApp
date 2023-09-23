package com.example.todoapp.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todoapp.DataBase.Dao.TasksDao
import com.example.todoapp.DataBase.Entities.Tasks

@Database(entities = [Tasks::class], version = 2)
abstract class AppDataBase: RoomDatabase() {
    abstract fun getDao(): TasksDao

    companion object{
        var instance: AppDataBase? = null
        fun getInstance(context: Context):AppDataBase{
            if (instance == null){
                instance = Room.databaseBuilder(context, AppDataBase::class.java, "app_db").allowMainThreadQueries().build()
            }
            return instance!!
        }
    }
}