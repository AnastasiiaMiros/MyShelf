package com.example.myshelf

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ImportContacts
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myshelf.ui.navigation.ShelfNavHost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShelfApp(navController: NavHostController = rememberNavController()){
    Scaffold(
        bottomBar = { ShelfAppBar(navController = navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            ShelfNavHost(navController = navController)
        }
    }
}

enum class ShelfScreenType(val title: String) {
    Search(title = "search"),
    Wishlist(title = "wishlist"),
    Reading(title = "reading"),
    Finished(title = "finished")
}

@Composable
fun ShelfAppBar(
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier,
        content = {
            BottomNavigationItem(
                icon = {
                    Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search"
                )
                },
                selected = currentRoute == ShelfScreenType.Search.title,
                onClick = {
                    navController.navigate(ShelfScreenType.Search.title)
                })
            BottomNavigationItem(
                icon = {
                    Icon(
                    imageVector = Icons.Filled.FavoriteBorder,
                    contentDescription = "Wishlist"
                )
                },
                selected = currentRoute == ShelfScreenType.Wishlist.title,
                onClick = {
                    navController.navigate(ShelfScreenType.Wishlist.title)
                })
            BottomNavigationItem(
                icon = {
                    Icon(
                    imageVector = Icons.Filled.ImportContacts,
                    contentDescription = "Reading"
                )
                },
                selected = currentRoute == ShelfScreenType.Reading.title,
                onClick = {
                    navController.navigate(ShelfScreenType.Reading.title)
                })
            BottomNavigationItem(
                icon = {
                    Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = "Finished"
                )
                },
                selected = currentRoute == ShelfScreenType.Finished.title,
                onClick = {
                    navController.navigate(ShelfScreenType.Finished.title)
                }
            )
        }
    )
}