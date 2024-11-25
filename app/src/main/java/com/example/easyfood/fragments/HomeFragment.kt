package com.example.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.easyfood.adapter.MostPopularAdapter
import com.example.easyfood.databinding.FragmentHomeBinding
import com.example.easyfood.pojo.CategoryMeals
import com.example.easyfood.pojo.Meal
import com.example.easyfood.ui.MealActivity
import com.example.easyfood.viewModel.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeMvvm: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var mostPopularAdapter: MostPopularAdapter

    companion object {
        const val MEAL_ID = "package com.example.easyfood.fragments.idMeal"
        const val MEAL_NAME = "package com.example.easyfood.fragments.nameMeal"
        const val MEAL_THUMB = "package com.example.easyfood.fragments.thumbMeal"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeMvvm = ViewModelProvider(this)[HomeViewModel::class.java]
        mostPopularAdapter = MostPopularAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preparePopularItemsRecyclerView()

        homeMvvm.getRandomMeal()
        observeRandomMeal()
        onRandomMealClicked()

        homeMvvm.getPopularItems()
        observePopularItemsLiveData()

        onPopularItemClicked()
    }

    private fun preparePopularItemsRecyclerView() {
        binding.recyclerPopularMeal.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = mostPopularAdapter
        }
    }

    private fun onRandomMealClicked() {
        binding.imageRandomMeal.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observeRandomMeal() {
        homeMvvm.observeRandomMealLiveData().observe(viewLifecycleOwner, object : Observer<Meal> {
            override fun onChanged(value: Meal) {
                Glide.with(this@HomeFragment)
                    .load(value.strMealThumb)
                    .into(binding.imageRandomMeal)
                randomMeal = value
            }
        })
    }

    private fun observePopularItemsLiveData() {
        homeMvvm.observePopularItemLiveData().observe(viewLifecycleOwner, object : Observer<List<CategoryMeals>> {
            override fun onChanged(value: List<CategoryMeals>) {
                mostPopularAdapter.setMeals(mealsList = value as ArrayList)
            }
        })
    }

    private fun onPopularItemClicked() {
        mostPopularAdapter.onItemClick = { categoryMeals ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, categoryMeals.idMeal)
            intent.putExtra(MEAL_NAME, categoryMeals.strMeal)
            intent.putExtra(MEAL_THUMB, categoryMeals.strMealThumb)
            startActivity(intent)
        }
    }
}