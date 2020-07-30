package com.example.todoapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.database.ListEntity

class HomeRecyclerAdapter(val context: Context, val list: List<ListEntity>, private val listener: OnListClickListener) : RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {
    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtList : TextView= view.findViewById(R.id.txtList)
        val txtPriority : TextView = view.findViewById(R.id.txtPriority)
        val btnDone : Button = view.findViewById(R.id.btnDone)
        val llContent : LinearLayout = view.findViewById(R.id.llContent)






    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_home_single_row, parent ,false)
        return HomeViewHolder(
            view
        )



    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface OnListClickListener{
        fun onRemoveListClick(list : ListEntity)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val list = list[position]


        holder.txtList.text = list.listContent


        holder.txtPriority.text = list.listPriority
//        Picasso.get().load(restaurant.resImage).error(R.drawable.default_book_cover).into(holder.imgResImage)
        if(list.listPriority == "High"){
           holder.llContent.setBackgroundResource(R.drawable.red
           )

        }else if(list.listPriority == "Moderate"){
            holder.llContent.setBackgroundResource(R.drawable.pink)


        }else if(list.listPriority == "Low"){
            holder.llContent.setBackgroundResource(R.drawable.cream)
        }



        holder.btnDone.setOnClickListener {
            listener.onRemoveListClick(list)

            val fragment = HomeFragment()

            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(
                    R.id.frameLayout,
                    fragment
                ).commit()

        }










    }


}
