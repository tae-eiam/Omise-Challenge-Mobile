package com.example.omisechallenge.ui.summary.viewholder

import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.RecyclerView
import com.example.omisechallenge.databinding.ViewAddressBinding

class AddressViewHolder(
    private val view: ViewAddressBinding,
    private val onAddressTextChangedListener: ((String) -> Unit)? = null
): RecyclerView.ViewHolder(view.root) {

    fun bind(address: String) {
        view.edtAddress.setText(address)
        view.edtAddress.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                onAddressTextChangedListener?.invoke(s.toString())
            }
        })
    }
}