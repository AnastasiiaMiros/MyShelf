package com.example.shelf.network

import com.example.myshelf.data.BookSearch
import com.example.myshelf.data.BookTrending
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://openlibrary.org/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface BookApiService {
    @GET("search.json")
    suspend fun getBooks(@Query("q")search: String) : BookSearch

    @GET("trending/daily.json")
    suspend fun getTrends() : BookTrending
}

object BookApi {
    val retrofitService : BookApiService by lazy {
        retrofit.create(BookApiService::class.java)
    }
}