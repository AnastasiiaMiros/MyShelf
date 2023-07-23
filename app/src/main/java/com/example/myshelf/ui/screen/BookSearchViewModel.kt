package com.example.myshelf.ui.screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshelf.data.BooksRepository
import com.example.myshelf.ui.BookUiState
import com.example.myshelf.ui.toSavedBook
import com.example.shelf.network.BookApi
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface SearchScreenUiState {
    object Search : SearchScreenUiState
    object Error : SearchScreenUiState
    object Loading : SearchScreenUiState
}

class BookSearchViewModel(private val booksRepository: BooksRepository) : ViewModel() {
    /** The mutable State that stores the status of the most recent page */
    var searchScreenUiState: SearchScreenUiState by mutableStateOf(SearchScreenUiState.Loading)
        private set

    var bookSearch by mutableStateOf("")
        private set

    var listResult: List<BookUiState> by mutableStateOf(mutableListOf())
        private set

    fun updateBookSearch(newQuery: String){
        bookSearch = newQuery
    }

    init {
        updateListResult()
    }

    fun updateListResult() {
        if (listResult.isNotEmpty() && bookSearch.isEmpty()) {
            searchScreenUiState = SearchScreenUiState.Search
        } else {
            viewModelScope.launch {
                try {
                    listResult = if (bookSearch.isEmpty()) {
                        BookApi.retrofitService.getTrends().works
                    } else {
                        BookApi.retrofitService.getBooks(bookSearch).docs
                    }
                    searchScreenUiState = SearchScreenUiState.Search
                } catch (e: IOException) {
                    searchScreenUiState = SearchScreenUiState.Error
                    Log.d("IO_Error", e.message ?: "idk")
                }
            }
        }
    }

    suspend fun saveBook(bookUiState: BookUiState, type: String){
        booksRepository.insertBook(bookUiState.toSavedBook(type))
    }
}