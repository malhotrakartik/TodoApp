package com.example.todoapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Lists")
data class ListEntity(

    @PrimaryKey val listId: String,
    @ColumnInfo(name = "list_content") val listContent : String,
    @ColumnInfo(name = "list_priority") val listPriority: String

//    @ColumnInfo(name = "restaurant_image") val resImage : String


)