package com.bignerdranch.android.bitebalance

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.room.TypeConverter
import androidx.room.Update
import com.google.gson.JsonSyntaxException
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import androidx.room.Query as rQuery


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
        @Query("q") query: String,
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

data class NutrientDetail(
    val label: String,
    val quantity: Float,
    val unit: String
)
data class TotalNutrients(
    val nutrients: Map<String, NutrientDetail>?
)

data class Digest(
    val label: String?,
    val tag: String?,
    val schemaOrgTag: String?,
    val total: Float?,
    val hasRDI: Boolean?,
    val daily: Float?,
    val unit: String?,
    val sub: List<Digest>?
)

@Entity(tableName = "recipes")
@TypeConverters(Converters::class)
data class Recipes(
    @PrimaryKey(autoGenerate = true) val dbId: Long = 0,
    val label: String,
    val image: String,
    val images: Images?,
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
    val totalNutrients: TotalNutrients?,
    val totalDaily: TotalNutrients?,
    val digest: List<Digest>?,
    val tags: List<String>?,
){
    val imageUrl: String?
        get() = images?.thumbnail?.url
}
data class Images(
    val thumbnail: Image?,
    val small: Image?,
    val regular: Image?,
    val large: Image?
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

class Converters {
    private val gson = Gson()

    // Converters for List<String>
    @TypeConverter
    fun fromStringList(value: String?): List<String>? {
        if (value == null) return null
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun toStringList(list: List<String>?): String? {
        if (list == null) return null
        return gson.toJson(list)
    }

    // Converters for Map<String, Image>
    @TypeConverter
    fun fromStringImageMap(value: String): Map<String, Image> {
        val mapType = object : TypeToken<Map<String, Image>>() {}.type
        return gson.fromJson(value, mapType)
    }

    @TypeConverter
    fun toStringImageMap(map: Map<String, Image>): String {
        return gson.toJson(map)
    }


    // Converters for List<Digest>
    @TypeConverter
    fun fromStringDigestList(value: String?): List<Digest>? {
        return try {
            val type = object : TypeToken<List<Digest>>() {}.type
            gson.fromJson<List<Digest>>(value, type)
        } catch (e: JsonSyntaxException) {
            // In case the JSON is not a List but an object, you can return null or handle it as needed
            null
        }
    }

    @TypeConverter
    fun toStringDigestList(list: List<Digest>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromImages(value: String?): Images? {
        return if (value == null) null else {
            val type = object : TypeToken<Images>() {}.type
            gson.fromJson(value, type)
        }
    }

    @TypeConverter
    fun toImages(images: Images?): String? {
        return gson.toJson(images)
    }

    @TypeConverter
    fun fromIngredientList(value: String?): List<Ingredients>? {
        return if (value == null || value.isEmpty()) null else {
            val type = object : TypeToken<List<Ingredients>>() {}.type
            gson.fromJson(value, type)
        }
    }

    @TypeConverter
    fun toIngredientList(ingredients: List<Ingredients>?): String? {
        return if (ingredients == null) null else gson.toJson(ingredients)
    }

    @TypeConverter
    fun fromTotalNutrients(value: String?): TotalNutrients? {
        return if (value == null) null else {
            val type = object : TypeToken<TotalNutrients>() {}.type
            gson.fromJson(value, type)
        }
    }

    @TypeConverter
    fun toTotalNutrients(nutrients: TotalNutrients?): String? {
        return gson.toJson(nutrients)
    }


}

@Dao
interface RecipesDao {
    @rQuery("SELECT * FROM recipes")
    fun getAllRecipes(): List<Recipes>

    @Insert
    fun insertRecipe(recipe: Recipes)

    @Update
    fun updateRecipe(recipe: Recipes)

    @Delete
    fun deleteRecipe(recipe: Recipes)
}

@Database(entities = [Recipes::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipesDao(): RecipesDao
}


