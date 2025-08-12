package com.example.omisechallenge.ui.product.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.omisechallenge.databinding.ViewProductItemBinding
import com.example.omisechallenge.ui.model.Order
import com.example.omisechallenge.ui.product.listener.OnEventTypeListener

class ProductViewHolder(
    private val view: ViewProductItemBinding,
    private val onViewHolderEventListener: ((OnEventTypeListener) -> Unit)? = null
) : RecyclerView.ViewHolder(view.root) {
    private lateinit var order: Order

    fun bind(order: Order) {
        this.order = order
        initViewListeners()

        with(view) {
            Glide.with(root)
                .load(order.imageUrl)
                .circleCrop()
                .into(imgProduct)

            tvProductName.text = order.name
            tvProductPrice.text = order.price.toString()
            tvAmount.text = order.amount.toString()
        }
    }

    private fun initViewListeners() {
        with(view) {
            imgAdd.setOnClickListener {
                val amount = calculateAmount(order.amount, 1)
                tvAmount.text = amount.toString()
                order.amount = amount
                onViewHolderEventListener?.invoke(OnEventTypeListener.OnAddOrderClickListener(order))
            }

            imgRemove.setOnClickListener {
                val amount = calculateAmount(order.amount, -1)
                tvAmount.text = amount.toString()
                order.amount = amount
                onViewHolderEventListener?.invoke(OnEventTypeListener.OnRemoveOrderClickListener(order))
            }
        }
    }

    private fun calculateAmount(amount: Int, num: Int): Int {
        val result = amount + num
        return if (result >= 0) result else 0
    }
}