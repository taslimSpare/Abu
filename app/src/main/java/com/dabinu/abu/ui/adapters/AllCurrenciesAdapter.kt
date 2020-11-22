package com.dabinu.abu.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dabinu.abu.R

class AllCurrenciesAdapter(private val symbolsAndNames: List<String>):
        RecyclerView.Adapter<AllCurrenciesAdapter.CurrencyViewHolder>() {

    inner class CurrencyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val tvSymbol = itemView.findViewById<TextView>(R.id.tv_currency)
        private val tvName = itemView.findViewById<TextView>(R.id.tv_name)

        fun bind(currency: String) {
            tvSymbol.text = currency.split("___")[0]
            tvName.text = currency.split("___")[1]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CurrencyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_currencies, parent, false))

    override fun getItemCount() = symbolsAndNames.size

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind(symbolsAndNames[position])
    }
}