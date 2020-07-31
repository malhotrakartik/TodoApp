package com.example.todoapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.view.*
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.todoapp.database.ListDatabase
import com.example.todoapp.database.ListEntity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    lateinit var spinner : Spinner
    lateinit var txtResult : TextView

    lateinit var etTask : EditText
    lateinit var btnAdd : Button
    lateinit var recyclerHome : RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: HomeRecyclerAdapter
    lateinit var sharedPreferences : SharedPreferences

    var dbList = listOf<ListEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        setHasOptionsMenu(true)
        recyclerHome = view.findViewById(R.id.recyclerHome)

        layoutManager = LinearLayoutManager(activity)
        spinner = view.findViewById(R.id.spinner)
        txtResult = view.findViewById(R.id.txtResult)
        etTask = view.findViewById(R.id.etTask)
        btnAdd = view.findViewById(R.id.btnAdd)
        sharedPreferences = context!!.getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)


        val options = arrayOf("High", "Moderate" , "Low")

        spinner.adapter = context?.let { ArrayAdapter<String>(it,android.R.layout.simple_list_item_1 , options) }

        spinner.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                txtResult.text ="select an option"
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                txtResult.text = options.get(position)
            }

        }
        val uniqueID = UUID.randomUUID().toString()





        btnAdd.setOnClickListener {
            val listEntity = ListEntity(
                uniqueID,
                etTask.text.toString(),
                txtResult.text.toString()
            )
            println(listEntity)
            DBAsyncTask(activity as Context,listEntity,2).execute()
            val fragment = HomeFragment()

            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(
                    R.id.frameLayout,
                    fragment
                ).commit()




        }
        dbList = RetrieveFavourites(
            activity as Context
        ).execute().get()
        println("dblist is")
        println(dbList)

        recyclerAdapter =
            HomeRecyclerAdapter(
                activity as Context,
                dbList,
                object :
                    HomeRecyclerAdapter.OnListClickListener {


                    override fun onRemoveListClick(list: ListEntity) {

                        DBAsyncTask(
                            activity as Context,
                            list,
                            3
                        ).execute()


                    }





                })

        recyclerHome.adapter = recyclerAdapter
        recyclerHome.layoutManager = layoutManager




        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_dashboard, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item?.itemId
        if (id == R.id.logout) {

            val dialog = AlertDialog.Builder(context)
//                    dialog.setTitle("success")
            dialog.setMessage("Are you sure you want to logout,Your Todo list will be Removed")
            dialog.setPositiveButton("YES"){
                    text,listener ->

                FirebaseAuth.getInstance().signOut()
                activity?.finish()
                sharedPreferences.edit().clear().apply()
                for(list in dbList) {
                    DBAsyncTask(activity as Context,list,3

                    ).execute()
                }






                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
//                        Toast.makeText(this@MainActivity ,"hello" , Toast.LENGTH_LONG).show()





            }
            dialog.setNegativeButton("Cancel"){
                    text,listener ->
                println("ok")


            }
            dialog.create()
            dialog.show()



        }

        return super.onOptionsItemSelected(item)


    }

    class DBAsyncTask(val context: Context, val listEntity: ListEntity, val mode : Int) : AsyncTask<Void, Void, Boolean>(){

        /*mode 1 = check db if book is fav or not
         mode 2 = add to fav
         mode 3 = remove from fav*/

        val db = Room.databaseBuilder(context , ListDatabase::class.java, "lists-db").build()
        override fun doInBackground(vararg p0: Void?): Boolean {
            when(mode){
                1 ->{

                    val list : ListEntity? =db.listDao().getListById(listEntity.listId)
                    db.close()
                    return list!= null        //will return false if no book is present

                }

                2->{
                    db.listDao().insertList(listEntity)
                    db.close()
                    return true

                }
                3->{
                    db.listDao().deleteList(listEntity)
                    db.close()
                    return true

                }



            }
            return false
        }

    }

    class RetrieveFavourites(val context: Context) : AsyncTask<Void , Void ,List<ListEntity>>() {
        val db = Room.databaseBuilder(context , ListDatabase::class.java, "lists-db").build()
        override fun doInBackground(vararg params: Void?): List<ListEntity> {
            return db.listDao().getAllLists()

        }
    }
}
