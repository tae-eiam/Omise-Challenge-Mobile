package com.example.omisechallenge.ui.product.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.omisechallenge.databinding.ViewProductItemBinding
import com.example.omisechallenge.ui.model.Order
import com.example.omisechallenge.ui.product.listener.OnEventTypeListener
import com.example.omisechallenge.ui.product.listener.OnProductEventTypeListener
import com.example.omisechallenge.ui.product.viewholder.ProductViewHolder

class ProductAdapter(
    private var items: MutableList<Order> = mutableListOf()
) : RecyclerView.Adapter<ProductViewHolder>() {
    private var onProductEventTypeListener: OnProductEventTypeListener? = null

    private val onViewHolderEventListener: ((OnEventTypeListener) -> Unit) by lazy {
        { onEventTypeListener -> onProductEventTypeListener?.onEventCallback(onEventTypeListener) }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {
        return ProductViewHolder(
            view = ViewProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onViewHolderEventListener = onViewHolderEventListener
        )
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

    fun updateData(orders: List<Order>) {
        items.clear()
        items.addAll(orders)
        notifyDataSetChanged()
    }

    fun setOnProductEventTypeListener(onProductEventTypeListener: OnProductEventTypeListener) {
        this.onProductEventTypeListener = onProductEventTypeListener
    }
}