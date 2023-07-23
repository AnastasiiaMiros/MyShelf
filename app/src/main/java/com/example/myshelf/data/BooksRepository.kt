package com.example.myshelf.data

import kotlinx.coroutines.flow.Flow

interface BooksRepository {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllBooksByTypeStream(type: String): Flow<List<SavedBook>>


    fun getAllBooksStream(): Flow<List<SavedBook>>

    /**
     * Retrieve an item from the given data source that matches with the id.
     */
    fun getBookStream(key: String): Flow<SavedBook?>

    /**
     * Insert item in the data source
     */
    suspend fun insertBook(book: SavedBook)

    /**
     * Delete item from the data source
     */
    suspend fun deleteBook(book: SavedBook)

    /**
     * Update item in the data source
     */
    suspend fun updateBook(book: SavedBook)
}