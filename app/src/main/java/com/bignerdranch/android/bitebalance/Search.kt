package com.bignerdranch.android.bitebalance

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Search.newInstance] factory method to
 * create an instance of this fragment.
 */
class Search : Fragment() {
    private lateinit var recipeViewModel: RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        recipeViewModel = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)

        var searchView = view.findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    recipeViewModel.fetchRecipes(query)
                    navigateToHomeFragment()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // If you don't need to handle text changes, just return false
                return false
            }

        })

        return view
    }




    private fun navigateToHomeFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, Home())
            .addToBackStack(null)
            .commit()
    }



}