package com.example.myshelf.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myshelf.data.BookSaveType
import com.example.myshelf.ui.screen.FinishedDestination
import com.example.myshelf.ui.screen.ReadingDestination
import com.example.myshelf.ui.screen.SavedBooksScreen
import com.example.myshelf.ui.screen.SearchDestination
import com.example.myshelf.ui.screen.SearchScreen
import com.example.myshelf.ui.screen.WishlistDestination

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun ShelfNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = SearchDestination.route,
        modifier = modifier
    ) {
        composable(route = SearchDestination.route) {
            SearchScreen()
        }
        composable(route = WishlistDestination.route) {
            SavedBooksScreen(
                title = BookSaveType.Wishlist.name
            )
        }
        composable(route = ReadingDestination.route) {
            SavedBooksScreen(
                title = BookSaveType.Reading.name
            )
        }
        composable(route = FinishedDestination.route) {
            SavedBooksScreen(
                title = BookSaveType.Finished.name
            )
        }
    }
}
