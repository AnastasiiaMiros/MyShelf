/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.myshelf.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myshelf.ShelfApplication
import com.example.myshelf.ui.screen.BookSearchViewModel
import com.example.myshelf.ui.screen.SavedBooksViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for BookSearchViewModel
        initializer {
            BookSearchViewModel(shelfApplication().container.booksRepository)
        }

        // Initializer for HomeViewModel
        initializer {
            SavedBooksViewModel(shelfApplication().container.booksRepository)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [ShelfApplication].
 */
fun CreationExtras.shelfApplication(): ShelfApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as ShelfApplication)
