package com.bignerdranch.android.bitebalance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class RecipeFragment : Fragment() {

    private lateinit var recipeName: TextView
    private lateinit var recipeImage: ImageView
    private lateinit var recipeIngredients: TextView
    private lateinit var recipeInstructions: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_clicked_recipe, container, false)

        recipeName = view.findViewById(R.id.recipeName)
        recipeImage = view.findViewById(R.id.recipeImage)
        recipeIngredients = view.findViewById(R.id.ingredients)
        recipeInstructions = view.findViewById(R.id.instructions)

        // Populate the views with your data

        return view
    }

    companion object {
        fun newInstance(): RecipeFragment {
            return RecipeFragment()
        }
    }
}