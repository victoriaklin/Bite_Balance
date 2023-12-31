package com.bignerdranch.android.bitebalance

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.bitebalance.MyApplication.Companion.db
import com.bignerdranch.android.bitebalance.databinding.ActivityHomeBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.google.android.material.imageview.ShapeableImageView
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt
import com.bumptech.glide.request.target.Target

class HomeActivity : AppCompatActivity() {

    //private lateinit var recyclerView: RecyclerView
    //private lateinit var searchView: SearchView
    //private lateinit var recipeButton: Button
    private lateinit var binding : ActivityHomeBinding
    private lateinit var recipeViewModel: RecipeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recipeViewModel = ViewModelProvider(this)[RecipeViewModel::class.java]


        navigateToFragment(Home(), RecipeViewModel.ViewState.HOME, "")

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.savedRecipes -> navigateToFragment(Home(), RecipeViewModel.ViewState.SAVED_RECIPES, "Home")
                R.id.home -> navigateToFragment(Home(), RecipeViewModel.ViewState.HOME, "Home")
                R.id.search -> navigateToFragment(Search(), RecipeViewModel.ViewState.SEARCH, "Search")
                else -> false
            }
            true
        }

   /*     // Doesnt work anymore
        recyclerView = findViewById(R.id.recyclerView)


        // Initialize RecyclerView with empty List
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RecipeAdapter(emptyList())

        searchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                // User submitted the search query
                query?.let { fetchRecipes(it) }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                // Text in SearchView has changed
                return true
            }

        })

        recipeButton.setOnClickListener {
            onRecipeButtonClick()
        }
*/
        //recyclerView.layoutManager = LinearLayoutManager(this)
        //updateRecyclerViewFromDB()
    }

    private fun navigateToFragment(fragment: Fragment, state: RecipeViewModel.ViewState, tag: String) {
        recipeViewModel.setCurrentState(state)
        supportFragmentManager.beginTransaction().apply {
            var existingFragment = supportFragmentManager.findFragmentByTag(tag)
            if (existingFragment == null) {
                existingFragment = fragment
                add(R.id.frameLayout, existingFragment, tag)
            } else {
                // Specific handling for Home fragment when in SAVED_RECIPES state
                if (state == RecipeViewModel.ViewState.SAVED_RECIPES && existingFragment is Home) {
                    existingFragment.loadSavedRecipes()
                }
            }
            supportFragmentManager.fragments.forEach {
                if (it != existingFragment && it.isAdded) {
                    hide(it)
                }
            }
            show(existingFragment)
            commit()
        }
    }


    private fun onRecipeButtonClick() {
        recipeViewModel.fetchRecipes("")
    }


    /* companion object {
         private const val APP_ID = "b849eb52"
         private const val APP_KEY = "883ec561ba2fc8d99515e7a58fc6455e"
     }*/
    /*private fun fetchRecipes(query: String) {
            lifecycleScope.launch {
                try {
                    var isRandom = true
                    if(query.isNotEmpty()){
                        isRandom = false
                    }
                    val response =
                        RecipeApi.retrofitService.getRecipes(query, APP_ID, APP_KEY, "public", isRandom)
                    if (response.isSuccessful && response.body() != null) {
                        val apiResponse = response.body()!!
                        val stringBuilder = StringBuilder()

                        val recipesForDb = apiResponse.hits.map { hit ->
                            Recipes(
                                label = hit.recipe.label,
                                image = hit.recipe.image,
                                images = hit.recipe.images?.let { Gson().toJson(it) },
                                source = hit.recipe.source,
                                url = hit.recipe.url,
                                dietLabels = hit.recipe.dietLabels?.let { Gson().toJson(it) },
                                healthLabels = hit.recipe.healthLabels?.let { Gson().toJson(it) },
                                cautions = hit.recipe.cautions?.let { Gson().toJson(it) },
                                ingredientLines = hit.recipe.ingredientLines?.let { Gson().toJson(it) },
                                ingredients = hit.recipe.ingredients?.let { Gson().toJson(it) },
                                calories = hit.recipe.calories,
                                totalWeight = hit.recipe.totalWeight,
                                totalTime = hit.recipe.totalTime,
                                cuisineType = hit.recipe.cuisineType?.let { Gson().toJson(it) },
                                mealType = hit.recipe.mealType?.let { Gson().toJson(it) },
                                dishType = hit.recipe.dishType?.let { Gson().toJson(it) },
                                totalNutrients = hit.recipe.totalNutrients?.let { Gson().toJson(it) },
                                totalDaily = hit.recipe.totalDaily?.let { Gson().toJson(it) },
                                digest = hit.recipe.digest?.let { Gson().toJson(it) },
                                tags = hit.recipe.tags?.let { Gson().toJson(it) }
                            )
                        }

                        // Insert into database
                        db?.let { db ->
                            recipesForDb.forEach { recipe ->
                                db.recipesDao().insertRecipe(recipe)
                            }
                        }
                        apiResponse.hits.forEach { hit ->
                            val recipe = hit.recipe
                            val caloriesRounded = recipe.calories.roundToInt()
                            stringBuilder.append("${recipe.label}: $caloriesRounded calories\n")
                        }

                        val recipeText =
                            if (stringBuilder.isNotEmpty()) stringBuilder.toString() else "No recipes found"

                        val fetchedRecipes = db?.recipesDao()?.getAllRecipes()
                        *//*fetchedRecipes?.let {
                            runOnUiThread {
                                updateRecyclerView(it)
                                updateRecyclerViewFromDB()
                            }
                        }*//*
                    } else {
                        runOnUiThread {
                            textViewRecipe.text = "No recipes found"
                        }
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
*/
    /*private fun updateRecyclerViewFromDB() {
        lifecycleScope.launch {
            val fetchedRecipes = MyApplication.db?.recipesDao()?.getAllRecipes()
            fetchedRecipes?.let { recipes ->
                runOnUiThread {
                    updateRecyclerView(recipes)
                }
            }
        }
    }*/

    /*private fun updateRecyclerView(recipes: List<Recipes>) {
        recyclerView.adapter = RecipeAdapter(recipes)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }*/



    /*private fun onRecipeButtonClick() {
        fetchRecipes("")
    }*/
}

class RecipeAdapter(private val context: Context, private var recipes: List<Recipes>, private val itemClickListener: (Recipes) -> Unit) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {
    class RecipeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.tvHeading)
        val imageView: ImageView = view.findViewById(R.id.title_image)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.titleTextView.text = recipe.label
        Glide.with(context).load(recipe.image).into(holder.imageView)

        holder.itemView.setOnClickListener {
            itemClickListener(recipe)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return RecipeViewHolder(view)
    }


    override fun getItemCount() = recipes.size

    fun updateRecipes(newRecipes: List<Recipes>) {
        recipes = newRecipes
        notifyDataSetChanged()  // Notify the RecyclerView to re-render
    }
}