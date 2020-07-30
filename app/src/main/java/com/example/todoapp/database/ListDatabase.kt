package com.example.todoapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ListEntity::class], version = 1)
abstract class ListDatabase : RoomDatabase(){

    abstract fun listDao() : ListDao



}