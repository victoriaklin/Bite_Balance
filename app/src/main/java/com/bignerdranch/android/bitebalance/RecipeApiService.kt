package com.bignerdranch.android.bitebalance

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.edamam.com/api/recipes/v2?type=public"
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()


interface RecipeApiService {
    @GET("recipes")
    suspend fun getRecipes(
        @Query("q") query: String,
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String
    ): Response<RecipeResponse> // RecipeResponse is a data class to handle the API response
}

object RecipeApi {
    val retrofitService : RecipeApiService by lazy {
        retrofit.create(RecipeApiService::class.java) }
}

data class RecipeResponse(
    val from: Int,
    val to: Int,
    val count: Int,
    val _links: Links,
    val hits: List<Hit>
)

data class Hit(
    val recipe: Recipe
)

data class Links(
    val next: Next?
)

data class Next(
    val href: String,
    val title: String
)

