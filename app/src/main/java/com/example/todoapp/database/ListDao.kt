package com.example.todoapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ListDao {

    @Insert
    fun insertList(listEntity: ListEntity)

    @Delete
    fun deleteList(listEntity: ListEntity)

    @Query("SELECT * FROM Lists")
    fun getAllLists() : List<ListEntity>

    @Query("DELETE FROM Lists WHERE listId=:listId")
    fun deleteListById(listId : String)


    @Query("SELECT * FROM Lists WHERE listId=:list_id")
    fun getListById(list_id : String): ListEntity

}