package com.example.todoapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class RegisterActivity : AppCompatActivity() {

    lateinit var btnRegister : Button
    lateinit var signIn : TextView
    lateinit var txtName : TextView
    lateinit var txtEmail : TextView
    lateinit var txtPhone : TextView
    lateinit var txtPassword : TextView
    lateinit var googleSignInClient : GoogleSignInClient
    lateinit var btnGoogle : SignInButton
    lateinit var sharedPreferences: SharedPreferences
    private var RC_SIGN_IN = 1



    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnRegister = findViewById(R.id.btnRegister)
        signIn = findViewById(R.id.signIn)
        txtName = findViewById(R.id.txtName)
        txtEmail = findViewById(R.id.txtEmail)
        txtPhone = findViewById(R.id.txtPhone)
        txtPassword = findViewById(R.id.txtPassword)
        btnGoogle = findViewById(R.id.btnGoogle)
        auth = FirebaseAuth.getInstance()
        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        btnRegister.setOnClickListener {
            signUpUser()

        }
        signIn.setOnClickListener {
            val intent = Intent(this@RegisterActivity , LoginActivity::class.java)
            startActivity(intent)
            finish()

        }
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
            startActivity(intent)
            finish()

        }





//         Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

//         Build a GoogleSignInClient with the options specified by gso.
            googleSignInClient = GoogleSignIn.getClient(this,gso)




        btnGoogle.setOnClickListener {

            signIn()

        }




   }
    private fun signUpUser() {
        if (txtEmail.text.toString().isEmpty()) {
            txtEmail.error = "Please enter email"
            txtEmail.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail.text.toString()).matches()) {
            txtEmail.error = "Please enter valid email"
            txtEmail.requestFocus()
            return
        }

        if (txtPassword.text.toString().isEmpty()) {
            txtPassword.error = "Please enter password"
            txtPassword.requestFocus()
            return
        }



        auth.createUserWithEmailAndPassword(txtEmail.text.toString(), txtPassword.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this,LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(baseContext, "Sign Up failed. Try again after some time.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Toast.makeText(this@RegisterActivity,"Signed IN successfully" , Toast.LENGTH_LONG).show()

                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this,"Sign IN unsuccessfull" , Toast.LENGTH_LONG).show()


                // ...
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    val user = auth.currentUser
                    if (user != null) {
                        updateUI(user)
                        val intent = Intent(this@RegisterActivity , MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                } else {

                    Toast.makeText(this@RegisterActivity,"Signed in failed" , Toast.LENGTH_LONG).show()


                    updateUI(null)
                }

                // ...
            }
    }


    private fun updateUI(fUser: FirebaseUser?) {


        //getLastSignedInAccount returned the account
        val account =
            GoogleSignIn.getLastSignedInAccount(applicationContext)
        if (account != null) {
            val personName = account.displayName
            val personGivenName = account.givenName
            val personEmail = account.email

            val personId = account.id
            Toast.makeText(this@RegisterActivity, "$personName  $personEmail", Toast.LENGTH_LONG)
                .show()
            sharedPreferences.edit()
                .putString("user_name", personName)
                .apply()
            sharedPreferences.edit()
                .putString("user_email", personEmail)
                .apply()
            sharedPreferences.edit()
                .putString("user_phone", personGivenName)
                .apply()
            sharedPreferences.edit()
                .putBoolean("isLoggedIn", true)
                .apply()


        }
    }


}
