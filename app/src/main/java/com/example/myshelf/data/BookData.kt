package com.example.myshelf.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myshelf.ui.BookUiState

data class BookSearch (
    val numFound : Int,
    val start : Int,
    val numFoundExact : Boolean,
    val docs : List<BookUiState>,
    val num_found : Int,
    val q : String
    )

data class BookTrending(
    val query : String,
    val works: List<BookUiState>,
    val days : Int,
    val hours : Int
)

enum class BookSaveType(){
    Wishlist(),
    Reading(),
    Finished()
}

@Entity(tableName = "saved_books")
data class SavedBook(
    @PrimaryKey
    val key : String,
    val title : String,
    val author_names: String,
    val cover_edition_key : String? = null,
    val notes : String = "",
    val type : String = BookSaveType.Wishlist.name
)