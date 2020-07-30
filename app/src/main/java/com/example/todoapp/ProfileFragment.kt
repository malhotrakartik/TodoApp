package com.example.todoapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.todoapp.R

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    lateinit var txtProfileName : TextView
    lateinit var txtProfileNumber : TextView
    lateinit var txtProfileEmail : TextView

    lateinit var sharedPreferences : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_profile, container, false)

        sharedPreferences = context!!.getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)


        txtProfileName = view.findViewById(R.id.txtProfileName)
        txtProfileNumber = view.findViewById(R.id.txtProfileNumber)
        txtProfileEmail = view.findViewById(R.id.txtProfileEmail)


        txtProfileName.text = sharedPreferences.getString("user_name","Your Name").toString()
        txtProfileNumber.text = sharedPreferences.getString("user_phone" , "your number").toString()
        txtProfileEmail.text = sharedPreferences.getString("user_email","Your email").toString()



        return view
    }

}
