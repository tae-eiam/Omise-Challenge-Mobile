package com.example.omisechallenge.ui.product.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.omisechallenge.databinding.ViewProductItemBinding
import com.example.omisechallenge.domain.model.Product

class ProductViewHolder(
    private val view: ViewProductItemBinding
) : RecyclerView.ViewHolder(view.root) {

    fun bind(product: Product) {
        with(view) {
            Glide.with(root.context)
                .load(product.imageUrl)
                .circleCrop()
                .into(imgProduct)

            tvProductName.text = product.name
            tvProductPrice.text = product.price.toString()
        }
    }
}