package com.example.omisechallenge.ui.summary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.omisechallenge.databinding.ViewAddressBinding
import com.example.omisechallenge.databinding.ViewOrderItemBinding
import com.example.omisechallenge.ui.model.Order
import com.example.omisechallenge.ui.summary.viewholder.AddressViewHolder
import com.example.omisechallenge.ui.summary.viewholder.OrderItemViewHolder

class SummaryAdapter(
    private var items: List<Order> = listOf(),
    private var address: String = ""
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var onAddressTextChangedListener: ((String) -> Unit)? = null

    private enum class ViewType(val type: Int) {
        ORDER(1),
        ADDRESS(2)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            ViewType.ORDER.type -> OrderItemViewHolder(
                ViewOrderItemBinding.inflate(inflater, parent, false)
            )
            else -> AddressViewHolder(
                ViewAddressBinding.inflate(inflater, parent, false),
                onAddressTextChangedListener
            )
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when(holder) {
            is OrderItemViewHolder -> holder.bind(items[position])
            is AddressViewHolder -> holder.bind(address)
            else -> Unit
        }
    }

    override fun getItemCount(): Int {
        return items.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return when(position) {
            itemCount - 1 -> ViewType.ADDRESS.type
            else -> ViewType.ORDER.type
        }
    }

    fun setOnAddressTextChangedListener(onAddressTextChangedListener: ((String) -> Unit)) {
        this.onAddressTextChangedListener = onAddressTextChangedListener
    }
}