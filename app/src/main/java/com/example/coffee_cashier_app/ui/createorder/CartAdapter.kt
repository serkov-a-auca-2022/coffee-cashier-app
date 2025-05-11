package com.example.coffee_cashier_app.ui.createorder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coffee_cashier_app.databinding.ItemCartBinding
import com.example.coffee_cashier_app.model.Item

class CartAdapter(
    private val cart: MutableMap<Item, Int>,
    private val onQuantityChange: (Item, Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.VH>() {

    private val items: List<Item>
        get() = cart.keys.toList()

    inner class VH(val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            val qty = cart[item] ?: 0
            binding.tvName.text = item.name
            binding.tvQuantity.text = qty.toString()
            binding.btnPlus.setOnClickListener {
                onQuantityChange(item, qty + 1)
            }
            binding.btnMinus.setOnClickListener {
                onQuantityChange(item, qty - 1)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(b)
    }

    override fun getItemCount(): Int = cart.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }
}
