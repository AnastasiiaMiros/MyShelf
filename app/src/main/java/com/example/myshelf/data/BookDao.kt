package com.example.myshelf.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(book: SavedBook)

    @Update
    suspend fun update(book: SavedBook)

    @Delete
    suspend fun delete(book: SavedBook)

    @Query("SELECT * from saved_books WHERE 'key' = :key")
    fun getBook(key: String): Flow<SavedBook>

    @Query("SELECT * from saved_books WHERE type = :type")
    fun getAllBooksByType(type: String): Flow<List<SavedBook>>

    @Query("SELECT * from saved_books")
    fun getAllBooks(): Flow<List<SavedBook>>

}