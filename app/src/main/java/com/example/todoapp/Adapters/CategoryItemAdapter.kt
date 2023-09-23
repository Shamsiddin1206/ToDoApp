package com.example.todoapp.Adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R

class CategoryItemAdapter(var list: MutableList<String>, var onPressed: OnPressed): RecyclerView.Adapter<CategoryItemAdapter.MyViewHolder>() {
    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val nomi: TextView = view.findViewById(R.id.category_item_name)
        val card: CardView = view.findViewById(R.id.category_item_cardview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val a = list.get(position)
        holder.nomi.text = a
        holder.card.setOnClickListener{
            for (i in 0 until list.size){
                if (i!=position){
                    holder.nomi.setTextColor(Color.parseColor("#000000"))
                    holder.card.setCardBackgroundColor(Color.parseColor("#C6E6FB"))
                    notifyDataSetChanged()
                }else{
                    holder.nomi.setTextColor(Color.parseColor("#ffffff"))
                    holder.card.setCardBackgroundColor(Color.parseColor("#2196F3"))
                    notifyDataSetChanged()
                }
            }
            onPressed.onPressed(a)
        }
    }

    interface OnPressed{
        fun onPressed(string: String)
    }
}