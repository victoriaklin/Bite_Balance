package com.bignerdranch.android.bitebalance

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.edamam.com/api/recipes/v2/"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .build()


interface RecipeApiService {

    companion object {
        val FIELDS = listOf(
            "label", "image", "images", "source", "url", "dietLabels", "healthLabels",
            "cautions", "ingredientLines", "ingredients", "calories", "glycemicIndex",
            "totalWeight", "totalTime", "cuisineType", "mealType", "dishType",
            "totalNutrients", "totalDaily", "digest", "tags"
        )
        val DIET = listOf(
           "balanced"
        )
    }
    @GET(".")
    suspend fun getRecipes(
        // @Query("q") query: String,
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String,
        @Query("type") type: String,
        @Query("random") random: Boolean,
        @Query("field") fields: List<String> = FIELDS,
        @Query("diet") diet: List<String> = DIET
    ): Response<ApiResponse>
}

object RecipeApi {
    val retrofitService : RecipeApiService by lazy {
        retrofit.create(RecipeApiService::class.java) }
}
data class Image(
    val url: String,
    val width: Int,
    val height: Int
)

data class Ingredients(
    val text: String,
    val quantity: Float?,
    val measure: String?,
    val food: String,
    val weight: Float?,
    val foodCategory: String?,
    val foodId: String,
    val image: String?
)

data class NutrientInfo(
    val label: String,
    val quantity: Float,
    val unit: String
)

data class Digest(
    val label: String,
    val tag: String,
    val schemaOrgTag: String?,
    val total: Float,
    val hasRDI: Boolean,
    val daily: Float,
    val unit: String,
    val sub: List<Digest>?
)

data class Recipes(
    val label: String,
    val image: String,
    val images: Map<String, Image>?,
    val source: String,
    val url: String,
    val dietLabels: List<String>?,
    val healthLabels: List<String>?,
    val cautions: List<String>?,
    val ingredientLines: List<String>?,
    val ingredients: List<Ingredients>?,
    val calories: Float,
    val totalWeight: Float,
    val totalTime: Int,
    val cuisineType: List<String>?,
    val mealType: List<String>?,
    val dishType: List<String>?,
    val totalNutrients: Map<String, NutrientInfo>?,
    val totalDaily: Map<String, NutrientInfo>?,
    val digest: List<Digest>?,
    val tags: List<String>?
)

data class Link(
    val href: String,
    val title: String
)

data class Hit(
    val recipe: Recipes,
    val _links: Map<String, Link>?
)

data class ApiResponse(
    val from: Int,
    val to: Int,
    val count: Int,
    val _links: Map<String, Link>?,
    val hits: List<Hit>
)


