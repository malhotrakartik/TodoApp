package com.example.todoapp.database

import android.os.FileObserver.DELETE
import androidx.room.*

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

    @Query("UPDATE Lists SET list_content =:content WHERE listId = :list_id")
   fun updateList(list_id: String,content:String)


    @Query("SELECT * FROM Lists WHERE listId=:list_id")
    fun getListById(list_id : String): ListEntity

}