package com.example.myshelf.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshelf.data.BooksRepository
import com.example.myshelf.data.SavedBook
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SavedBooksViewModel (private val booksRepository: BooksRepository): ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    private lateinit var bookshelfUiState: StateFlow<BookshelfUiState>

    fun getList(type: String) : StateFlow<BookshelfUiState>{
        bookshelfUiState = booksRepository.getAllBooksByTypeStream(type)
            .map { BookshelfUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = BookshelfUiState()
            )
        return bookshelfUiState
    }

    suspend fun updateBook(book: SavedBook, newType: String?, newNotes: String?){
        booksRepository.updateBook(SavedBook(
            key = book.key,
            title = book.title,
            author_names = book.author_names,
            cover_edition_key = book.cover_edition_key,
            notes = newNotes ?: book.notes,
            type = newType ?: book.type
        ))
    }

    suspend fun deleteBook(book: SavedBook){
        booksRepository.deleteBook(book)
    }
}

data class BookshelfUiState(val bookList: List<SavedBook> = listOf())
