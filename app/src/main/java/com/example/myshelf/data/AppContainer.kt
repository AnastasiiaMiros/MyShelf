package com.example.myshelf.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val booksRepository: BooksRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineBooksRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [BooksRepository]
     */
    override val booksRepository: BooksRepository by lazy {
        OfflineBooksRepository(BookDatabase.getDatabase(context).bookDao())
    }
}