package com.bignerdranch.android.bitebalance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

class ClickedRecipeFragment : Fragment() {

    private var recipe: Recipes? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            recipe = it.getParcelable("recipe")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_clicked_recipe, container, false)

        val recipeNameTextView: TextView = view.findViewById(R.id.recipeName)
        val recipeImageView: ImageView = view.findViewById(R.id.recipeImage)
        val ingredientsTextView: TextView = view.findViewById(R.id.ingredients)
        val instructionsTextView: TextView = view.findViewById(R.id.instructions)

        recipe?.let {
            recipeNameTextView.text = it.label
            Glide.with(this).load(it.image).into(recipeImageView)

            val ingredientsText =
                it.ingredientLines?.joinToString(separator = "\n") // Each ingredient in a new line
            ingredientsTextView.text = ingredientsText

        }

        return view
    }

    companion object {
        fun newInstance(recipe: Recipes): ClickedRecipeFragment {
            val args = Bundle()
            args.putParcelable("recipe", recipe)
            val fragment = ClickedRecipeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
