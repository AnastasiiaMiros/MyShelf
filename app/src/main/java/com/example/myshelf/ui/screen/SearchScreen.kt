package com.example.myshelf.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ImportContacts
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.compose.MyShelfTheme
import com.example.inventory.ui.navigation.NavigationDestination
import com.example.myshelf.R
import com.example.myshelf.data.BookSaveType
import com.example.myshelf.ui.AppViewModelProvider
import com.example.myshelf.ui.BookUiState
import kotlinx.coroutines.launch


object SearchDestination : NavigationDestination {
    override val route = "search"
}

@Composable
fun SearchScreen(
    viewModel: BookSearchViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(id = R.string.explore),
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
        )
        SearchField(
            userSearch = viewModel.bookSearch,
            onUserInputChanged = viewModel::updateBookSearch,
            onKeyboardDone = viewModel::updateListResult
        )
        when(viewModel.searchScreenUiState){
            is SearchScreenUiState.Loading -> LoadingScreen()
            is SearchScreenUiState.Search -> BookList(booklist = viewModel.listResult)
            is SearchScreenUiState.Error -> ErrorScreen()
        }
    }
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier){
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(R.drawable.ic_connection_error),
            contentDescription = "Loading"
        )
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier){
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(R.drawable.loading_img),
            contentDescription = "Loading"
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(
    userSearch: String,
    onUserInputChanged: (String) -> Unit,
    onKeyboardDone: () -> Unit
){
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = userSearch,
        label = { Text(stringResource(id = R.string.search)) },
        onValueChange = onUserInputChanged,
        singleLine = true,
        trailingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = stringResource(id = R.string.press_to_search),
                tint = MaterialTheme.colorScheme.secondary
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { focusManager.clearFocus(); onKeyboardDone() }
        ),
        modifier = Modifier.padding(
            start = dimensionResource(id = R.dimen.padding_medium),
            bottom = dimensionResource(id = R.dimen.padding_medium)
        )
    )
}


@Composable
fun BookList(
    booklist: List<BookUiState>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier){
        items(booklist){
            BookCard(book = it)
        }
    }
}


fun AuthorsToString(authors: List<String>) : String{
    var concat = ""
    if (authors.size > 1) concat = "; "
    var result = ""
    for (author in authors){
        result += author + concat
    }
    return result
}

@Composable
fun BookCard( book: BookUiState){
    Card(
        modifier = Modifier.padding(
            top = dimensionResource(id = R.dimen.padding_small),
            bottom = dimensionResource(id = R.dimen.padding_small),
            start = dimensionResource(id = R.dimen.padding_medium),
            end = dimensionResource(id = R.dimen.padding_medium)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_small))
        ){
            BookCover(cover_edition_key = book.cover_edition_key)
            Column {
                BookInfo(book_title = book.title, book_authors = AuthorsToString(book.author_name))
                ListButtons(book)
            }
        }
    }
}


@Composable
fun BookCover(cover_edition_key: String?){
    if(cover_edition_key.isNullOrBlank()){
        Image(
            painter = painterResource(id = R.drawable.no_image_found),
            contentDescription = null,
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.image_size))
                .padding(dimensionResource(id = R.dimen.padding_medium))
                .aspectRatio(4f / 5f)
                .clip(MaterialTheme.shapes.small),
            contentScale = ContentScale.Crop
        )
    }
    else {
        val bookCoverUrl = "https://covers.openlibrary.org/b/olid/$cover_edition_key-M.jpg?default=false"
        AsyncImage(
            model = bookCoverUrl,
            contentDescription = null,
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.image_size))
                .padding(dimensionResource(id = R.dimen.padding_medium))
                .aspectRatio(4f / 5f)
                .clip(MaterialTheme.shapes.small),
            contentScale = ContentScale.Crop
        )
    }
}


@Composable
fun BookInfo(book_title: String, book_authors: String){
    Column {
        Text(
            text = book_title,
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
        )
        Text(
            text = book_authors,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_small))
        )
    }
}


@Composable
fun ListButtons(
    book: BookUiState,
    viewModel: BookSearchViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val coroutineScope = rememberCoroutineScope()
    var isInWishlist by remember { mutableStateOf(false) }
    var isReading by remember { mutableStateOf(false) }
    var isFinished by remember { mutableStateOf(false) }
    Row{
        IconToggleButton(
            checked = isInWishlist,
            onCheckedChange = {
                isInWishlist = !isInWishlist
                isReading = false
                isFinished = false
                if(isInWishlist){
                    coroutineScope.launch{
                        viewModel.saveBook(book, BookSaveType.Wishlist.name)
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
                        viewModel.saveBook(book, BookSaveType.Reading.name)
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
                        viewModel.saveBook(book, BookSaveType.Finished.name)
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


@Preview(showSystemUi = true)
@Composable
fun SearchScreenPreview(){
    MyShelfTheme(){
        SearchScreen(
        )
    }
}
