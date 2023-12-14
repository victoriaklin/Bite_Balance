package com.bignerdranch.android.bitebalance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast

class FoodRestrictionActivity : AppCompatActivity() {

    private val user = User()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_restriction)

        val checkBoxLactose = findViewById<CheckBox>(R.id.lactose)
        val checkBoxVegetarian = findViewById<CheckBox>(R.id.vegetarian)
        val checkBoxVegan = findViewById<CheckBox>(R.id.vegan)
        val checkBoxGlutenFree = findViewById<CheckBox>(R.id.gluten)
        val checkBoxKosher = findViewById<CheckBox>(R.id.kosher)
        val checkBoxTreeNuts = findViewById<CheckBox>(R.id.tree_nuts)
        val checkBoxPeanuts = findViewById<CheckBox>(R.id.peanuts)
        val checkBoxSesame = findViewById<CheckBox>(R.id.sesame)
        val checkBoxEggs = findViewById<CheckBox>(R.id.eggs)
        val checkBoxSoy = findViewById<CheckBox>(R.id.soy)

        checkBoxLactose.setOnCheckedChangeListener { _, isChecked ->
            user.lactoseIntolerant = isChecked
        }

        checkBoxVegetarian.setOnCheckedChangeListener { _, isChecked ->
            user.vegetarian = isChecked
        }
        checkBoxVegan.setOnCheckedChangeListener { _, isChecked ->
            user.vegan = isChecked
        }
        checkBoxGlutenFree.setOnCheckedChangeListener { _, isChecked ->
            user.glutenFree = isChecked
        }
        checkBoxKosher.setOnCheckedChangeListener { _, isChecked ->
            user.kosher = isChecked
        }
        checkBoxTreeNuts.setOnCheckedChangeListener { _, isChecked ->
            user.treeNuts = isChecked
        }
        checkBoxPeanuts.setOnCheckedChangeListener { _, isChecked ->
            user.peanuts = isChecked
        }
        checkBoxSesame.setOnCheckedChangeListener { _, isChecked ->
            user.sesame = isChecked
        }
        checkBoxEggs.setOnCheckedChangeListener { _, isChecked ->
            user.eggs = isChecked
        }
        checkBoxSoy.setOnCheckedChangeListener { _, isChecked ->
            user.soy = isChecked
        }

        val done = findViewById<Button>(R.id.done_button)
        done.setOnClickListener{
            val openHomePage = Intent(this, HomeActivity::class.java)
            startActivity(openHomePage)
        }
    }
}