package com.example.todoapp.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.DataBase.AppDataBase
import com.example.todoapp.DataBase.Entities.Tasks
import com.example.todoapp.R
import com.example.todoapp.Utils.ItemTouchHelperAdapter

class SingleTaskAdapter(var list: MutableList<Tasks>, var context: Context, var onPressed: OnPressed): RecyclerView.Adapter<SingleTaskAdapter.MyViewHolder>(), ItemTouchHelperAdapter {
    val appDatabase: AppDataBase by lazy {
        AppDataBase.getInstance(context)
    }
    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var nomi: TextView = view.findViewById(R.id.task_item_name)
        var card: CardView = view.findViewById(R.id.task_item_card)
        var button: RadioButton = view.findViewById(R.id.task_item_select)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val a = list[position]
        holder.nomi.text = a.task_name
        holder.button.isChecked = a.mode
        holder.button.setOnClickListener {
            if (a.mode){
                holder.button.isChecked = false
                a.mode = false
                notifyDataSetChanged()
            }else{
                holder.button.isChecked = true
                a.mode = true
                notifyDataSetChanged()
            }
            appDatabase.getDao().updateTask(a)
        }
        holder.itemView.setOnClickListener {
            onPressed.onPressed(a)
        }
    }

    interface OnPressed{
        fun onPressed(tasks: Tasks)
    }

    override fun onItemDismiss(Position: Int) {
        list.removeAt(Position)
        appDatabase.getDao().deleteTask(list[Position])
        notifyItemRemoved(Position)
    }

}