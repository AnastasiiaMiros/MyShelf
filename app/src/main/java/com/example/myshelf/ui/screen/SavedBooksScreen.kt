package com.example.myshelf.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ImportContacts
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.ui.navigation.NavigationDestination
import com.example.myshelf.R
import com.example.myshelf.data.SavedBook
import com.example.myshelf.ui.AppViewModelProvider
import kotlinx.coroutines.launch

object WishlistDestination : NavigationDestination {
    override val route = "wishlist"
}

object ReadingDestination : NavigationDestination {
    override val route = "reading"
}

object FinishedDestination : NavigationDestination {
    override val route = "finished"
}

@Composable
fun SavedBooksScreen(
    title: String,
    viewModel: SavedBooksViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val bookshelfUiState by viewModel.getList(title).collectAsState()
    Column (horizontalAlignment = Alignment.CenterHorizontally){
        Text(
            text = title,
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
        )
        SavedBookList(booklist = bookshelfUiState.bookList, title)
    }
}

@Composable
fun SavedBookList(booklist: List<SavedBook>, type: String, modifier: Modifier = Modifier){
    if(booklist.isEmpty()){
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize()
        ) {
            Text(text = "No saved books of type: $type")
        }
    } else {
        LazyColumn(){
            items(booklist){
                SavedBookCard(book = it)
            }
        }
    }
}

@Composable
fun SavedBookCard(book: SavedBook){
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.padding(
            top = dimensionResource(id = R.dimen.padding_small),
            bottom = dimensionResource(id = R.dimen.padding_small),
            start = dimensionResource(id = R.dimen.padding_medium),
            end = dimensionResource(id = R.dimen.padding_medium)
        )
    ) {
        Column{
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.padding_small))
            ) {
                BookCover(cover_edition_key = book.cover_edition_key)
                Column {
                    BookInfo(book_title = book.title, book_authors = book.author_names)
                    Row {
                        SavedListButtons(book)
                        Spacer(modifier = Modifier.weight(1f))
                        BookNotesButton(
                            expanded = expanded,
                            onClick = { expanded = !expanded }
                        )
                    }
                }
            }
            if (expanded) {
                NotesDetailsBody(book)
            }
        }
    }
}

@Composable
fun SavedListButtons(
    book: SavedBook,
    viewModel: SavedBooksViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val coroutineScope = rememberCoroutineScope()
    var isInWishlist by remember { mutableStateOf(false) }
    var isReading by remember { mutableStateOf(false) }
    var isFinished by remember { mutableStateOf(false) }
    if(book.type == "Wishlist"){
        isInWishlist = true
    }
    if(book.type == "Reading"){
        isReading = true
    }
    if(book.type == "Finished"){
        isFinished = true
    }
    Row{
        IconToggleButton(
            checked = isInWishlist,
            onCheckedChange = {
                isInWishlist = !isInWishlist
                isReading = false
                isFinished = false
                if(isInWishlist){
                    coroutineScope.launch{
                        viewModel.updateBook(book = book, newType = "Wishlist", newNotes = null)
                    }
                } else {
                    coroutineScope.launch {
                        viewModel.deleteBook(book)
                    }
                }
            }
        ) {
            Icon(
                imageVector = if (isInWishlist) {
                    Icons.Filled.Favorite
                } else {
                    Icons.Default.FavoriteBorder
                },
                contentDescription = null
            )
        }

        IconToggleButton(
            checked = isReading,
            onCheckedChange = {
                isInWishlist = false
                isReading = !isReading
                isFinished = false
                if(isReading){
                    coroutineScope.launch{
                        viewModel.updateBook(book = book, newType = "Reading", newNotes = null)
                    }
                } else {
                    coroutineScope.launch {
                        viewModel.deleteBook(book)
                    }
                }
            }
        ) {
            Icon(
                imageVector = if (isReading) {
                    Icons.Filled.ImportContacts
                } else {
                    Icons.Default.ImportContacts
                },
                contentDescription = null
            )
        }

        IconToggleButton(
            checked = isFinished,
            onCheckedChange = {
                isInWishlist = false
                isReading = false
                isFinished = !isFinished
                if(isFinished){
                    coroutineScope.launch{
                        viewModel.updateBook(book = book, newType = "Finished", newNotes = null)
                    }
                } else {
                    coroutineScope.launch {
                        viewModel.deleteBook(book)
                    }
                }
            }
        ) {
            Icon(
                imageVector = if (isFinished) {
                    Icons.Filled.CheckCircle
                } else {
                    Icons.Default.CheckCircle
                },
                contentDescription = null
            )
        }
    }
}


@Composable
private fun BookNotesButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = stringResource(R.string.expand_button_notes),
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotesDetailsBody(
    book: SavedBook,
    viewModel: SavedBooksViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val coroutineScope = rememberCoroutineScope()
    var inputValue: String by remember {
        mutableStateOf(book.notes)
    }
    var actionEnabled by remember { mutableStateOf(false) } 
    Column{
        OutlinedTextField(
            value = inputValue,
            onValueChange = {
                inputValue = it
                actionEnabled = true
                            },
            label = { androidx.compose.material.Text(stringResource(R.string.notes)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium)),
            singleLine = false
        )
        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.updateBook(book, newType = null, newNotes = inputValue)
                }
            },
            enabled = actionEnabled,
            modifier = Modifier
                .align(Alignment.End)
                .padding(
                    start = dimensionResource(id = R.dimen.padding_small),
                    bottom = dimensionResource(id = R.dimen.padding_small),
                    end = dimensionResource(id = R.dimen.padding_medium)
                )
        ) {
            Text(
                stringResource(R.string.save_action),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}