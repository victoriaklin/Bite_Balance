package com.bignerdranch.android.bitebalance
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipeViewModel : ViewModel() {
    enum class ViewState {
        HOME, SEARCH, SAVED_RECIPES
    }

    private var currentState: ViewState = ViewState.HOME

    fun setCurrentState(state: ViewState) {
        currentState = state
    }

    fun getCurrentState(): ViewState {
        return currentState
    }

    companion object {
        private const val APP_ID = "b849eb52"
        private const val APP_KEY = "883ec561ba2fc8d99515e7a58fc6455e"
    }

    // LiveData to hold the recipes
    private val _recipes = MutableLiveData<List<Recipes>>()
    val recipes: LiveData<List<Recipes>> = _recipes

    fun fetchRecipes(query: String) {
             viewModelScope.launch(Dispatchers.IO) {
                try {
                    var isRandom = true
                    if(query.isNotEmpty()){
                        isRandom = false
                    }
                    val response =
                        RecipeApi.retrofitService.getRecipes(query,
                            RecipeViewModel.APP_ID,
                            RecipeViewModel.APP_KEY, "public", isRandom)
                    if (response.isSuccessful && response.body() != null) {
                        val apiResponse = response.body()!!
                        val stringBuilder = StringBuilder()

                        val recipesForApi = apiResponse.hits.map { hit ->
                            Recipes(
                                label = hit.recipe.label,
                                image = hit.recipe.image,
                                images = hit.recipe.images,
                                source = hit.recipe.source,
                                url = hit.recipe.url,
                                dietLabels = hit.recipe.dietLabels ?: emptyList(),
                                healthLabels = hit.recipe.healthLabels ?: emptyList(),
                                cautions = hit.recipe.cautions ?: emptyList(),
                                ingredientLines = hit.recipe.ingredientLines ?: emptyList(),
                                ingredients = hit.recipe.ingredients ?: emptyList(),
                                calories = hit.recipe.calories,
                                totalWeight = hit.recipe.totalWeight,
                                totalTime = hit.recipe.totalTime,
                                cuisineType = hit.recipe.cuisineType ?: emptyList(),
                                mealType = hit.recipe.mealType ?: emptyList(),
                                dishType = hit.recipe.dishType ?: emptyList(),
                                totalNutrients = hit.recipe.totalNutrients ?: TotalNutrients(null),
                                totalDaily = hit.recipe.totalDaily ?: TotalNutrients(null),
                                digest = hit.recipe.digest ?: emptyList(),
                                tags = hit.recipe.tags ?: emptyList()
                            )
                        }

                        // Update LiveData with new recipes
                        withContext(Dispatchers.Main) {
                            _recipes.value = recipesForApi
                        }
                        // Insert into database ONLY if saved
                       /* MyApplication.db?.let { db ->
                            recipesForDb.forEach { recipe ->
                                db.recipesDao().insertRecipe(recipe)
                            }
                        }*/

                        // Gets all saved recipes in database, will use later
                        /* val fetchedRecipes = MyApplication.db?.recipesDao()?.getAllRecipes()
                        fetchedRecipes?.let { fetchedRecipes ->
                            withContext(Dispatchers.Main) {
                                _recipes.value = fetchedRecipes
                            }
                        }*/
                        /*if (fetchedRecipes != null) {
                            _recipes.postValue(fetchedRecipes)
                        }*/
                        _recipes.postValue(recipesForApi)
                    } else {
                        // If API call fails do this
                        Log.e(
                            "API Error",
                            "Response not successful: ${response.errorBody()?.string()}"
                        )
                    }
                } catch (e: Exception) {
                    Log.e("API Error", "Exception: ${e.message}")
                }

            }

        }


    }
