package com.example.newmeetapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*


class ProfileActivity : AppCompatActivity() {

    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")
        loadProfile()

        val apiKey = getString(R.string.api_key)

        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }





    }




    @SuppressLint("SetTextI18n")
    private fun loadProfile()
    {
        val user = auth.currentUser
        val userReference = databaseReference?.child(user?.uid!!)

        emailText.text = "Email -- " + user?.email
        userReference?.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot)
            {
                passwordText.text = snapshot.child("password").value.toString()
                firstnameText.text = "First name -- " + snapshot.child("firstname").value.toString()
                lastnameText.text = "Last name -- " +snapshot.child("lastname").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun logoutClickButton(view: View)
    {
        auth.signOut()
        startActivity(Intent(this@ProfileActivity, LoginActivity::class.java))
        finish()
    }

    fun goMapButton(view: View)
    {
        startActivity(Intent(this@ProfileActivity, MapsActivity::class.java))
    }
}
