package com.example.myshelf.ui

import com.example.myshelf.data.SavedBook
import com.example.myshelf.ui.screen.AuthorsToString

data class BookUiState (
    val key : String = "",
    val title : String = "",
    val first_publish_year : Int = 0,
    val number_of_pages_median : Int = 0,
    val cover_edition_key : String? = null,
    val publisher : List<String> = listOf(""),
    val language : List<String> = listOf("English"),
    val author_name : List<String> = listOf("")
    )

fun BookUiState.toSavedBook(type: String): SavedBook = SavedBook(
    key = key,
    title = title,
    author_names = AuthorsToString(author_name),
    cover_edition_key = cover_edition_key,
    notes = "",
    type = type
)