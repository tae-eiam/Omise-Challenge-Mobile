package com.example.omisechallenge.ui.summary.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.omisechallenge.R
import com.example.omisechallenge.databinding.ViewOrderItemBinding
import com.example.omisechallenge.ui.model.Order

class OrderItemViewHolder(
    private val view: ViewOrderItemBinding
): RecyclerView.ViewHolder(view.root) {

    fun bind(order: Order) {
        with(view) {
            Glide.with(root)
                .load(order.imageUrl)
                .circleCrop()
                .into(imgProduct)

            tvProductName.text = order.name
            tvProductPrice.text = order.price.toString()
            tvAmount.text = root.context.getString(R.string.num_order, order.amount.toString())
        }
    }
}