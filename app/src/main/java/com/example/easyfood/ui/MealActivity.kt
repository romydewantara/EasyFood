package com.example.easyfood.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.easyfood.databinding.ActivityMealBinding
import com.example.easyfood.db.MealDatabase
import com.example.easyfood.fragments.HomeFragment
import com.example.easyfood.pojo.Meal
import com.example.easyfood.viewModel.MealViewModel
import com.example.easyfood.viewModel.MealViewModelFactory

class MealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealBinding

    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var youtubeLink: String
    private lateinit var mealMvvm: MealViewModel
    private var mealToSave: Meal? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge() //set status bar as transparent
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
        val mealDatabase = MealDatabase.getInstance(this@MealActivity)
        val mealViewModelFactory = MealViewModelFactory(mealDatabase)
        mealMvvm = ViewModelProvider(this, mealViewModelFactory)[MealViewModel::class.java]

        getMealInformationFromIntent()
        setMealInformation()

        mealMvvm.getMealDetails(mealId)
        observeMealDetailsLiveData()

        onFavoriteClick()
        onYoutubeClick()
    }

    private fun onFavoriteClick() {
        binding.buttonFavorite.setOnClickListener {
            mealToSave?.let {
                mealMvvm.insertMeal(it)
            }
        }
    }

    private fun onYoutubeClick() {
        binding.imageYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    private fun getMealInformationFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

    private fun setMealInformation() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imageMealDetail)
        binding.collapsingToolbarLayout.title = mealName
    }

    private fun observeMealDetailsLiveData() {
        mealMvvm.observeMealLiveData().observe(this, object : Observer<Meal> {
            override fun onChanged(value: Meal) {
                binding.textCategoryMeal.text = "Category: ${value.strCategory}"
                binding.textAreaMeal.text = "Area: ${value.strArea}"
                binding.textInstructions.text = value.strInstructions
                mealToSave = value
                youtubeLink = value.strYoutube
            }
        })
    }

}