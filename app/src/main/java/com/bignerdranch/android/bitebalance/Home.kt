package com.bignerdranch.android.bitebalance

import android.os.Bundle
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        myAdapter = RecipeAdapter(requireContext(), emptyList())
        recyclerView.adapter = myAdapter

        // Get ViewModel
        val recipeViewModel = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)
        recipeViewModel.recipes.observe(viewLifecycleOwner) { newRecipes ->
            // Update the adapter
            myAdapter.updateRecipes(newRecipes)
        }
        updateRecyclerViewFromDB()

        return view
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
}
