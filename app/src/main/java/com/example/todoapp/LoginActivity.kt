package com.example.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {


    lateinit var txtEmailLogin : TextView
    lateinit var txtPasswordLogin : TextView
    lateinit var btnLogin : Button
    lateinit var txtRegister  :TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        txtEmailLogin = findViewById(R.id.txtEmailLogin)
        txtPasswordLogin = findViewById(R.id.txtPasswordLogin)
        btnLogin = findViewById(R.id.btnLogin)
        txtRegister = findViewById(R.id.txtRegister)

        auth = FirebaseAuth.getInstance()
        btnLogin.setOnClickListener {
            doLogin()
        }
        txtRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity , RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    private fun doLogin(){
        if (txtEmailLogin.text.toString().isEmpty()) {
            txtEmailLogin.error = "Please enter email"
            txtEmailLogin.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(txtEmailLogin.text.toString()).matches()) {
            txtEmailLogin.error = "Please enter valid email"
            txtEmailLogin.requestFocus()
            return
        }

        if (txtPasswordLogin.text.toString().isEmpty()) {
            txtPasswordLogin.error = "Please enter password"
            txtPasswordLogin.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(txtEmailLogin.text.toString(), txtPasswordLogin.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(baseContext, "Login failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                    // ...
                }

                // ...
            }
    }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }
    private fun updateUI(currentUser : FirebaseUser?){

        if(currentUser != null){
            val intent = Intent(this@LoginActivity , MainActivity::class.java)
            startActivity(intent)
            finish()

//            Toast.makeText(baseContext, "Login Done.",
//                Toast.LENGTH_SHORT).show()

        }else{
            Toast.makeText(baseContext, "Login failed.",
                Toast.LENGTH_SHORT).show()

        }


    }
}
