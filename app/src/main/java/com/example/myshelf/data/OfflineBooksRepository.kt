package com.example.myshelf.data

import kotlinx.coroutines.flow.Flow

class OfflineBooksRepository(private val bookDao: BookDao) : BooksRepository {
    override fun getAllBooksByTypeStream(type: String): Flow<List<SavedBook>> = bookDao.getAllBooksByType(type)

    override fun getAllBooksStream(): Flow<List<SavedBook>> = bookDao.getAllBooks()

    override fun getBookStream(key: String): Flow<SavedBook?> = bookDao.getBook(key)

    override suspend fun insertBook(book: SavedBook) = bookDao.insert(book)

    override suspend fun deleteBook(book: SavedBook) = bookDao.delete(book)

    override suspend fun updateBook(book: SavedBook) = bookDao.update(book)
}