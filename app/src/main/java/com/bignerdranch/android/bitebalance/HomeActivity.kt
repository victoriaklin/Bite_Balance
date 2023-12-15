package com.bignerdranch.android.bitebalance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var recipeButton: Button
    private lateinit var textViewRecipe: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Set up recyclerView
        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)

        // Initialize RecyclerView with empty List
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RecipeAdapter(emptyList())

        // Generate recipe buttons
        recipeButton = findViewById(R.id.recipe_button)

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

        // Display recipe
        textViewRecipe = findViewById(R.id.textViewRecipe)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val menuItem = bottomNavigationView.menu.findItem(R.id.settings)
        menuItem.setOnMenuItemClickListener {
            val openSettingsPage = Intent(this@HomeActivity, SettingsActivity::class.java)
            startActivity(openSettingsPage)
            true
        }

    }
    companion object {
        private const val APP_ID = "b849eb52"
        private const val APP_KEY = "883ec561ba2fc8d99515e7a58fc6455e"
    }
    private fun fetchRecipes(query: String) {
        lifecycleScope.launch {
            try {
                val response = RecipeApi.retrofitService.getRecipes(APP_ID, APP_KEY, "public", true)
                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!
                    val stringBuilder = StringBuilder()

                    apiResponse.hits.forEach { hit ->
                        val recipe = hit.recipe
                        val caloriesRounded = recipe.calories.roundToInt()
                        stringBuilder.append("${recipe.label}: $caloriesRounded calories\n")
                    }

                    val recipeText = if (stringBuilder.isNotEmpty()) stringBuilder.toString() else "No recipes found"

                    runOnUiThread {
                        textViewRecipe.text = recipeText
                    }
                } else {
                    runOnUiThread {
                        textViewRecipe.text = "No recipes found"
                    }
                    Log.e("API Error", "Response not successful: ${response.errorBody()?.string()}")
                }
            }  catch (e: Exception) {
                Log.e("API Error", "Exception: ${e.message}")
            }
        }

        }

//        lifecycleScope.launch {
//            val response = RecipeApi.retrofitService.getRecipes(query, "b849eb52", "883ec561ba2fc8d99515e7a58fc6455e")
//            if (response.isSuccessful && response.body() != null) {
//                val recipes = response.body()!!.hits.map { it.recipe }
//                updateRecyclerView(recipes)
//            } else {
//                // error
//            }
//        }


    private fun updateRecyclerView(recipes: List<Recipe>) {
        recyclerView.adapter = RecipeAdapter(recipes)
    }

    private fun addDataToList() {

    }

    private fun onRecipeButtonClick() {
        fetchRecipes("")
    }
}

    class RecipeAdapter(private val recipes: List<Recipe>) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

        class RecipeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val titleTextView: TextView = view.findViewById(R.id.recipeTitleTextView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recipe_item, parent, false)
            return RecipeViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
            val recipe = recipes[position]
            holder.titleTextView.text = recipe.label
        }

        override fun getItemCount() = recipes.size
    }

