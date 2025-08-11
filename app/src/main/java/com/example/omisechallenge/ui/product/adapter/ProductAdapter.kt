package com.example.omisechallenge.ui.product.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.omisechallenge.databinding.ViewProductItemBinding
import com.example.omisechallenge.domain.model.Product
import com.example.omisechallenge.ui.product.viewholder.ProductViewHolder

class ProductAdapter(
    private var items: MutableList<Product> = mutableListOf()
): RecyclerView.Adapter<ProductViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {
        return ProductViewHolder(ViewProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateData(products: List<Product>) {
        Log.d("Tae", "Inside update data")
        items.clear()
        items.addAll(products)
        notifyDataSetChanged()
    }
}