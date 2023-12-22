package com.bignerdranch.android.bitebalance

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Home : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: RecipeAdapter
    private lateinit var recipeViewModel: RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipeViewModel = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        myAdapter = RecipeAdapter(requireContext(), emptyList()) { recipe ->
            openRecipeDetailsFragment(recipe)
        }

        recyclerView.adapter = myAdapter
        loadDataBasedOnState()
        return view
    }

    private fun loadDataBasedOnState() {
        when (recipeViewModel.getCurrentState()) {
            RecipeViewModel.ViewState.HOME -> showRecipesFromViewModel()
            RecipeViewModel.ViewState.SEARCH -> showRecipesFromViewModel()
            RecipeViewModel.ViewState.SAVED_RECIPES -> showRecipesFromDatabase()
            else -> {}
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recipeViewModel.recipes.observe(viewLifecycleOwner) { newRecipes ->
            myAdapter.updateRecipes(newRecipes)
        }
        myAdapter = RecipeAdapter(requireContext(), emptyList()) { recipe ->
            openRecipeDetailsFragment(recipe)
        }
    }
    private fun openRecipeDetailsFragment(recipe: Recipes) {
        val fragment = ClickedRecipeFragment.newInstance(recipe)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .addToBackStack(null) // to enable back navigation
            .commit()
    }

    override fun onResume() {
        super.onResume()
        if (recipeViewModel.getCurrentState() == RecipeViewModel.ViewState.HOME ||
            recipeViewModel.getCurrentState() == RecipeViewModel.ViewState.SEARCH) {
            showRecipesFromViewModel()
        }
        else{
            showRecipesFromDatabase()
        }
    }

    private fun refreshData() {
        when (recipeViewModel.getCurrentState()) {
            RecipeViewModel.ViewState.HOME,
            RecipeViewModel.ViewState.SEARCH -> {
                showRecipesFromViewModel()
            }
            RecipeViewModel.ViewState.SAVED_RECIPES -> {
                showRecipesFromDatabase()
            }
            else -> {}
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recipeViewModel.recipes.removeObservers(viewLifecycleOwner)
        recipeViewModel.recipes.observe(viewLifecycleOwner) { newRecipes ->
            myAdapter.updateRecipes(newRecipes)
        }
    }


    private fun updateRecyclerViewFromDB() {
        lifecycleScope.launch(Dispatchers.IO) {
            val recipes = MyApplication.db?.recipesDao()?.getAllRecipes()
            recipes?.let {
                withContext(Dispatchers.Main) {
                    myAdapter.updateRecipes(it)
                }
            }
        }
    }

    fun updateRecyclerViewFromVM() {
        recipeViewModel.recipes.value?.let { recipes ->
            myAdapter.updateRecipes(recipes)
        }
    }

    fun showRecipesFromViewModel() {
        recipeViewModel.recipes.value?.let { newRecipes ->
            myAdapter.updateRecipes(newRecipes)
        }
    }


    fun loadSavedRecipes() {
        recipeViewModel.setCurrentState(RecipeViewModel.ViewState.SAVED_RECIPES)
        showRecipesFromDatabase()
    }



    fun showRecipesFromDatabase() {
        lifecycleScope.launch(Dispatchers.IO) {
            val recipesFromDB = MyApplication.db?.recipesDao()?.getAllRecipes() ?: emptyList()
            withContext(Dispatchers.Main) {
                myAdapter.updateRecipes(recipesFromDB)
            }
        }
    }

}
