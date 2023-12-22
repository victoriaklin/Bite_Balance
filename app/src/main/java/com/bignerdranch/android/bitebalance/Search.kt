package com.bignerdranch.android.bitebalance

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider

class Search : Fragment() {
    private lateinit var recipeViewModel: RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipeViewModel = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recipeViewModel = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        val searchView = view.findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    recipeViewModel.fetchRecipes(it)
                    navigateToHomeFragment()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return view
    }

    private fun navigateToHomeFragment() {
        // Set the ViewModel state to reflect that we are going back to Home
        recipeViewModel.setCurrentState(RecipeViewModel.ViewState.HOME)

        val homeFragment = requireActivity().supportFragmentManager.findFragmentByTag("Home")
            ?: Home() // Create a new instance only if it doesn't exist

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, homeFragment, "Home")
            .commit()
    }
}
