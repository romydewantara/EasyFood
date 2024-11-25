package com.example.easyfood.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easyfood.databinding.CategoryLayoutBinding
import com.example.easyfood.pojo.Category

class CategoriesAdapter(): RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    private var categoryList = ArrayList<Category>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            CategoryLayoutBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(categoryList[position].strCategoryThumb)
            .into(holder.binding.imageCategory)
        holder.binding.textCategoryHome.text = categoryList[position].strCategory
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCategoryList(categoryList: List<Category>) {
        this.categoryList = categoryList as ArrayList<Category>
        notifyDataSetChanged()
    }

    inner class CategoryViewHolder(val binding: CategoryLayoutBinding): RecyclerView.ViewHolder(binding.root)
}