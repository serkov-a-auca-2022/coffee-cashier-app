package com.example.coffee_cashier_app.ui.createorder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coffee_cashier_app.databinding.ItemCategoryHeaderBinding
import com.example.coffee_cashier_app.model.Category

class CategoryAdapter(
    private val categories: List<Category>,
    private val onCategoryClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.VH>() {

    inner class VH(val binding: ItemCategoryHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cat: Category) {
            binding.tvCategoryName.text = cat.name
            binding.root.setOnClickListener { onCategoryClick(cat) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemCategoryHeaderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(b)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(categories[position])
    }
}
