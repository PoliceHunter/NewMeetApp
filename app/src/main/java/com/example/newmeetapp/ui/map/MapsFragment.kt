package com.example.newmeetapp.ui.map

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newmeetapp.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*

class MapsFragment : Fragment() {


    private lateinit var mMap: GoogleMap
    private lateinit var databaseReference: DatabaseReference
    var database: FirebaseDatabase? = null


    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */


        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("places")

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
        val moscow = LatLng(55.7522, 37.6156)
//        mMap.addMarker(
//            MarkerOptions()
//                .position(moscow)
//                .title("Marker in Moscow"))

        googleMap.addMarker(MarkerOptions().position(moscow).title("Marker in Moscow"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(moscow, 10f))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}