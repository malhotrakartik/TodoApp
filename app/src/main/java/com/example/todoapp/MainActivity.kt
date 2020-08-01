package com.example.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout : DrawerLayout
    lateinit var coordinateLayout : CoordinatorLayout
    lateinit var appToolbar : androidx.appcompat.widget.Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView : NavigationView


    private lateinit var auth: FirebaseAuth
    var previousMenuItem: MenuItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout = findViewById(R.id.drawerLayout)
        coordinateLayout = findViewById(R.id.coordinatorLayout)
        appToolbar = findViewById(R.id.appToolbar)
        frameLayout = findViewById(R.id.frameLayout)
        navigationView = findViewById(R.id.navigationView)




        auth = FirebaseAuth.getInstance()
//        btnLogOut.setOnClickListener {
//            FirebaseAuth.getInstance().signOut()
//            finish()
//            val intent = Intent(this@MainActivity , LoginActivity::class.java)
//            startActivity(intent)
//        }
        setUpToolBar()
        openHome()

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {

            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = false
            }

            it.isChecked = true
            this.previousMenuItem = it



            when (it.itemId) {
                R.id.home -> {
                    openHome()
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            ProfileFragment()
                        )

                        .commit()


                    drawerLayout.closeDrawers()

                    supportActionBar?.title = "Profile"
                }



            }
            return@setNavigationItemSelectedListener true
        }
    }



private fun setUpToolBar(){
    setSupportActionBar(appToolbar)
    supportActionBar?.title = "To Do List"
    supportActionBar?.setHomeButtonEnabled(true)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
}

 fun openHome(){
    val fragment = HomeFragment()
    val transaction = supportFragmentManager.beginTransaction()

    transaction
        .replace(R.id.frameLayout,fragment)

        .commit()
    drawerLayout.closeDrawers()

    supportActionBar?.title = "To-Do List"
    navigationView.setCheckedItem(R.id.home)

}

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frameLayout)
        when(frag) {

            !is HomeFragment -> openHome()

            else -> super.onBackPressed()
        }


    }

override fun onOptionsItemSelected(item: MenuItem): Boolean {

    val id=item.itemId

    if(id == android.R.id.home)
    {
        drawerLayout.openDrawer(GravityCompat.START)
    }
    return super.onOptionsItemSelected(item)
}
}
