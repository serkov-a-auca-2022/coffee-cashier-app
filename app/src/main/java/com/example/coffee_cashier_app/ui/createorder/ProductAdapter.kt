package com.example.coffee_cashier_app.ui.createorder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coffee_cashier_app.databinding.ItemMenuProductBinding
import com.example.coffee_cashier_app.model.Item

class ProductAdapter(
    private val onProductClick: (Item) -> Unit
) : RecyclerView.Adapter<ProductAdapter.VH>() {

    private var items: List<Item> = emptyList()

    inner class VH(val binding: ItemMenuProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.tvProductName.text = item.name
            binding.tvProductPrice.text = "${item.price} сом"
            binding.root.setOnClickListener { onProductClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemMenuProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(b)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    fun submitList(list: List<Item>) {
        items = list
        notifyDataSetChanged()
    }
}
