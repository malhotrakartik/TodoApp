package com.example.todoapp

import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.todoapp.database.ListDatabase
import com.example.todoapp.database.ListEntity

class HomeRecyclerAdapter(val context: Context, val list: List<ListEntity>, private val listener: OnListClickListener) : RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {
    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtList : TextView= view.findViewById(R.id.txtList)
        val txtPriority : TextView = view.findViewById(R.id.txtPriority)
        val btnDone : Button = view.findViewById(R.id.btnDone)
        val llContent : LinearLayout = view.findViewById(R.id.llContent)
//        val btnSpinner : Spinner = view.findViewById(R.id.btnSpinner)
//        val txtResult : TextView = view.findViewById(R.id.txtResult)
        val btnEdit : Button = view.findViewById(R.id.btnEdit)

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

        holder.btnEdit.setOnClickListener {
            var text : String = holder.txtList.text.toString()
            DBAsync(context, list.listId,text ).execute()

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


//            val options1 = arrayOf("High", "Moderate", "Low")
//
//            holder.btnSpinner.adapter = context?.let {
//                ArrayAdapter<String>(
//                    it,
//                    android.R.layout.simple_list_item_1,
//                    options1
//                )
//            }
//
//            holder.btnSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                override fun onNothingSelected(parent: AdapterView<*>?) {
//
//                    holder.txtResult.text = "select an option"
//                }
//
//                override fun onItemSelected(
//                    parent: AdapterView<*>?,
//                    view: View?,
//                    position: Int,
//                    id: Long
//                ) {
//                    holder.txtResult.text = options1.get(position)
//                    DBAsync(context, list.listId, options1.get(position)).execute()
////                val fragment = HomeFragment()
////
////                (context as MainActivity).supportFragmentManager.beginTransaction()
////                    .replace(
////                        R.id.frameLayout,
////                        fragment
////                    ).commit()
//
//
//                }
//
//            }
        }













    }



class DBAsync(
    context: Context,
    val listId: String,
    val listContent : String

) : AsyncTask<Void, Void, Boolean>() {
    val db = Room.databaseBuilder(context, ListDatabase::class.java, "lists-db").build()


    override fun doInBackground(vararg params: Void?): Boolean {
        db.listDao().updateList(listId,listContent)
        db.close()
        return true





    }

}
