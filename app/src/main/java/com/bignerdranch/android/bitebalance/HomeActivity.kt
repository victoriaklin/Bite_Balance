package com.bignerdranch.android.bitebalance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var recipeButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

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
    }

    private fun fetchRecipes(query: String) {
        lifecycleScope.launch {
            val response = RecipeApi.retrofitService.getRecipes(query, "b849eb52", "883ec561ba2fc8d99515e7a58fc6455e")
            if (response.isSuccessful && response.body() != null) {
                val recipes = response.body()!!.hits.map { it.recipe }
                updateRecyclerView(recipes)
            } else {
                // Handle errors here
            }
        }
    }

    private fun updateRecyclerView(recipes: List<Recipe>) {
        recyclerView.adapter = RecipeAdapter(recipes)
    }

    private fun addDataToList() {

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

