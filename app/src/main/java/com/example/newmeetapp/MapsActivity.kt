package com.example.newmeetapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.api.Status

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_registration.*
import kotlin.reflect.typeOf

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var databaseReference: DatabaseReference
    private lateinit var etPlace: Place
    var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // DataBase initailization
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("places")

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        val apiKey = getString(R.string.api_key)

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }

        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment =
                supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                        as AutocompleteSupportFragment

        autocompleteFragment.setCountries("RU")
        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS))


        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                savePlaceToDB(place)
                // TODO: Get info about the selected place.
                Log.i("autocomplete", "Place: ${place.name}, ${place.id},  ${place.latLng}, ${place.address}")
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i( "autocomplete", "An error occurred: $status")
            }
        })

    }

    private fun savePlaceToDB(place: Place)
    {
        // for pathString for place need unical id
        val currentUserDb = databaseReference.child(place.id.toString())
        currentUserDb.child("name").setValue(place.name)
        currentUserDb.child("id").setValue(place.id)
        currentUserDb.child("LatLng").setValue(place.latLng)
        currentUserDb.child("address").setValue(place.address.toString())
        Toast.makeText(this, "The place will be added to DataBase", Toast.LENGTH_SHORT).show()
    }

//    private fun addMarker(place: Place)
//    {
//        mMap.addMarker(
//                place.latLng?.let {
//                    MarkerOptions()
//                            .position(it)
//                            .title("${place.name}")
//                })
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(place.latLng))
//    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap)
    {
        //val user = auth.currentUser
        mMap = googleMap

        databaseReference.addValueEventListener(object: ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot)
            {
                for (postSnapshot in snapshot.children) {
                    // TODO: handle the post
                    val etPlaceId = postSnapshot.child("id").value.toString()
                    val etPlaceName = postSnapshot.child("name").value.toString()
                    val etPlaceLat : Double = postSnapshot.child("LatLng/latitude").value as Double
                    val etPlaceLng : Double = postSnapshot.child("LatLng/longitude").value as Double//.split(',')// (55.755826,37.6173)
                    val etPlaceAddress = postSnapshot.child("address").value.toString()
                    val moscowlog = LatLng(55.755826, 37.6173)
                    val etPlaceLatLng = LatLng(etPlaceLat, etPlaceLng)
                    Log.i("db reference info", "info ps - $postSnapshot,  ")
                    Log.i("get from bd", "Place: ${etPlaceName}, || $etPlaceLat - $etPlaceLng ,== $moscowlog  == $etPlaceLatLng||")
//                    if (moscowlog == etPlaceLatLng)
//                        Log.i("log complete", "all good")
                    mMap = googleMap
                    mMap.addMarker(
                        MarkerOptions()
                        .position(etPlaceLatLng)
                        .title(etPlaceName))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Error access to db", "All bad")
            }
        })
        // Add a marker in Sydney and move the camera
        val moscow = LatLng(55.7522, 37.6156)
//        mMap.addMarker(
//            MarkerOptions()
//                .position(moscow)
//                .title("Marker in Moscow"))
        mMap.maxZoomLevel
        mMap.moveCamera(CameraUpdateFactory.newLatLng(moscow))
    }
}