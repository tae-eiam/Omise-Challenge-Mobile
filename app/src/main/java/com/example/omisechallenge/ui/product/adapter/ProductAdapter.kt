package com.example.omisechallenge.ui.product.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.omisechallenge.databinding.ViewLoadingItemBinding
import com.example.omisechallenge.databinding.ViewProductItemBinding
import com.example.omisechallenge.ui.model.Order
import com.example.omisechallenge.ui.product.listener.OnEventTypeListener
import com.example.omisechallenge.ui.product.listener.OnProductEventTypeListener
import com.example.omisechallenge.ui.product.viewholder.LoadingViewHolder
import com.example.omisechallenge.ui.product.viewholder.ProductViewHolder

class ProductAdapter(
    private var items: MutableList<Order> = mutableListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var onProductEventTypeListener: OnProductEventTypeListener? = null
    private var isLoadMore: Boolean = false

    private val onViewHolderEventListener: ((OnEventTypeListener) -> Unit) by lazy {
        { onEventTypeListener -> onProductEventTypeListener?.onEventCallback(onEventTypeListener) }
    }

    private enum class ViewType(val type: Int) {
        ORDER(1),
        LOAD_MORE(2)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ViewType.ORDER.type -> ProductViewHolder(
                view = ViewProductItemBinding.inflate(inflater, parent, false),
                onViewHolderEventListener = onViewHolderEventListener
            )
            else -> LoadingViewHolder(
                view = ViewLoadingItemBinding.inflate(inflater, parent, false)
            )
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when (holder) {
            is ProductViewHolder -> holder.bind(items[position])
            is LoadingViewHolder -> holder.bind()
            else -> Unit
        }
    }

    override fun getItemCount(): Int {
        return if (isLoadMore) items.size + 1 else items.size
    }

    override fun getItemViewType(position: Int): Int {
        return when(position) {
            itemCount - 1 -> if (isLoadMore) ViewType.LOAD_MORE.type else ViewType.ORDER.type
            else -> ViewType.ORDER.type
        }
    }

    fun updateData(orders: List<Order>, isLoadMore: Boolean) {
        this.isLoadMore = isLoadMore
        items.clear()
        items.addAll(orders)
        notifyDataSetChanged()
    }

    fun setOnProductEventTypeListener(onProductEventTypeListener: OnProductEventTypeListener) {
        this.onProductEventTypeListener = onProductEventTypeListener
    }
}