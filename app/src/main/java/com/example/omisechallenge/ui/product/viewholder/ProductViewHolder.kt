package com.example.omisechallenge.ui.product.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.omisechallenge.databinding.ViewProductItemBinding
import com.example.omisechallenge.domain.model.Product
import com.example.omisechallenge.ui.product.listener.OnEventTypeListener

class ProductViewHolder(
    private val view: ViewProductItemBinding,
    private val onViewHolderEventListener: ((OnEventTypeListener) -> Unit)? = null
) : RecyclerView.ViewHolder(view.root) {
    private lateinit var product: Product

    fun bind(product: Product) {
        this.product = product
        initViewListeners()

        with(view) {
            Glide.with(root.context)
                .load(product.imageUrl)
                .circleCrop()
                .into(imgProduct)

            tvProductName.text = product.name
            tvProductPrice.text = product.price.toString()
        }
    }

    private fun initViewListeners() {
        with(view) {
            imgAdd.setOnClickListener {
                val amount = calculateAmount(tvAmount.text.toString(), 1)
                tvAmount.text = amount.toString()
                onViewHolderEventListener?.invoke(OnEventTypeListener.OnAddOrderClickListener(product, amount))
            }

            imgRemove.setOnClickListener {
                val amount = calculateAmount(tvAmount.text.toString(), -1)
                tvAmount.text = amount.toString()
                onViewHolderEventListener?.invoke(OnEventTypeListener.OnRemoveOrderClickListener(product, amount))
            }
        }
    }

    private fun calculateAmount(amount: String, num: Int): Int {
        val result = amount.toInt() + num
        return if (result >= 0) result else 0
    }
}