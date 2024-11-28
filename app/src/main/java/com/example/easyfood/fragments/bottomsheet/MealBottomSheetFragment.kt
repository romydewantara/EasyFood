package com.example.easyfood.fragments.bottomsheet

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.databinding.FragmentMealBottomSheetBinding
import com.example.easyfood.fragments.HomeFragment
import com.example.easyfood.ui.MainActivity
import com.example.easyfood.ui.MealActivity
import com.example.easyfood.viewModel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val MEAL_ID = "param1"

class MealBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentMealBottomSheetBinding
    private lateinit var viewModel: HomeViewModel
    private var mealId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        arguments?.let {
            mealId = it.getString(MEAL_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMealBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mealId?.let {
            viewModel.getMealById(it)
        }
        observeMealBottomSheet()
        onBottomSheetDialogClicked()
    }

    private fun onBottomSheetDialogClicked() {
        binding.bottomSheetDialog.setOnClickListener {
            if (mealName != null && mealThumb != null) {
                val intent = Intent(activity, MealActivity::class.java)
                intent.apply {
                    putExtra(HomeFragment.MEAL_ID, mealId)
                    putExtra(HomeFragment.MEAL_NAME, mealName)
                    putExtra(HomeFragment.MEAL_THUMB, mealThumb)
                }
                startActivity(intent)
            }
        }
    }

    private var mealName: String? = null
    private var mealThumb: String? = null

    private fun observeMealBottomSheet() {
        viewModel.observeBottomSheetMealsLiveData().observe(viewLifecycleOwner, Observer { meal ->
            Glide.with(requireContext()).load(meal.strMealThumb).into(binding.imageBottomSheet)
            binding.textAreaBottomSheet.text = meal.strArea
            binding.textCategoryBottomSheet.text = meal.strCategory
            binding.textMealNameBottomSheet.text = meal.strMeal

            mealName = meal.strMeal
            mealThumb = meal.strMealThumb
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            MealBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(MEAL_ID, param1)
                }
            }
    }
}