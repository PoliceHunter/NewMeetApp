package com.example.newmeetapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.libraries.places.api.Places
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {

    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_search,
                R.id.navigation_mapsFragment,
                R.id.navigation_creating,
                R.id.navigation_notifications,
                R.id.navigation_profile))
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //Firebase connect
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val apiKey = getString(R.string.api_key)
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }

//        bt_next.setOnClickListener{
//
//
//            // Initialize Place API
//            if (!Places.isInitialized()) {
//                Places.initialize(applicationContext, apiKey)
//            }
//            val autocompleteFragment = supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
//
//            autocompleteFragment.setCountries("RU")
//            // Specify the types of place data to return.
//            autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS))
//            autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
//                override fun onPlaceSelected(place: Place) {
//                    bt_next.setOnClickListener {
//                        savePlaceToDB(place)
//                    // TODO: Get info about the selected place.
//                    Log.i("autocomplete", "Place: ${place.name}, ${place.id},  ${place.latLng}, ${place.address}")
//                    }
//                }
//                override fun onError(status: Status) {
//                    // TODO: Handle the error.
//                    Log.i( "autocomplete", "An error occurred: $status")
//                }
//            })
//        }

        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */

    }


//    fun savePlaceToDB(place: Place)
//    {
//        // for pathString for place need unical id
//        databaseReference = database?.reference!!.child("profile")
//        val currentUserDb = databaseReference!!.child(place.id.toString())
//        currentUserDb.child("name").setValue(place.name)
//        currentUserDb.child("id").setValue(place.id)
//        currentUserDb.child("LatLng").setValue(place.latLng)
//        currentUserDb.child("address").setValue(place.address.toString())
//        Toast.makeText(this, "The place will be added to DataBase", Toast.LENGTH_SHORT).show()
//    }


//    @SuppressLint("SetTextI18n")
//    private fun loadProfile()
////    {
//        val user = auth.currentUser
//        val userReference = databaseReference?.child(user?.uid!!)
//
//        emailText.text = "Email -- " + user?.email
//        userReference?.addValueEventListener(object: ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot)
//            {
//                passwordText.text = snapshot.child("password").value.toString()
//                firstnameText.text = "First name -- " + snapshot.child("firstname").value.toString()
//                lastnameText.text = "Last name -- " +snapshot.child("lastname").value.toString()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//        })
//    }

    fun logoutClickButton(view: View)
    {
        auth.signOut()
        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        finish()
    }

    fun goMapButton(view: View)
    {
        startActivity(Intent(this@MainActivity, MapsActivity::class.java))
    }
}
