package com.bignerdranch.android.bitebalance

data class Recipe(
    val label: String,
    val image: String,
    val source: String,
    val url: String,
    val yield: Int,
    val dietLabels: List<String>,
    val healthLabels: List<String>,
    val ingredientLines: List<String>,
    val ingredients: List<Ingredient>,
    val calories: Double,
    val totalTime: Int
    // Will add others if needed
)

data class Ingredient(
    val text: String,
    val weight: Double
    // Will add others if needed
)
