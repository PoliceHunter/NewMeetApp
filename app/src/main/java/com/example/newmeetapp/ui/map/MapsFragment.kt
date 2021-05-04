package com.example.newmeetapp.ui.map

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.newmeetapp.R
import com.example.newmeetapp.ui.events.Events
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.database.*

class MapsFragment : Fragment() {


    private lateinit var mMap: GoogleMap
    private lateinit var databaseReference: DatabaseReference
    var database: FirebaseDatabase? = null
    private lateinit var eventArrayList : ArrayList<Events>


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
        databaseReference = database?.reference!!.child("events")

        mMap = googleMap

        eventArrayList = arrayListOf<Events>()
        databaseReference.addValueEventListener(object: ValueEventListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {
            override fun onDataChange(snapshot: DataSnapshot)
            {
                if (snapshot.exists()) {
                    for (postSnapshot in snapshot.children) {
                        var markerMap : Marker? = null
                        val event = postSnapshot.getValue(Events::class.java)
                        eventArrayList.add(event!!)
                        if (postSnapshot.child("place").exists()) {

                            val etPlaceLat: Double = event.place?.LatLng?.latitude!!
                            val etPlaceLng: Double = event.place?.LatLng?.longitude!!
                            val etPlaceLatLng = LatLng(etPlaceLat, etPlaceLng)
                            val nameMarker = event.name
                            markerMap = mMap.addMarker(
                                MarkerOptions()
                                        .icon(BitmapDescriptorFactory.defaultMarker(getColorMarker(event.category)))
                                        .position(etPlaceLatLng)
                                        .title(nameMarker)

                            )
                        }
                        if (markerMap != null) {
                            markerMap.tag = event
                        }
                        mMap.setOnMarkerClickListener(this)
                        mMap.setOnInfoWindowClickListener(this)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Error access to db", "All bad")
            }

            override fun onMarkerClick(marker: Marker): Boolean {

                val eventArray = marker.tag as? Events

                marker.showInfoWindow()
                if (eventArray != null) {
                    Toast.makeText(
                            requireContext(),
                            "${marker.title} has been clicked ${eventArray.date}.",
                            Toast.LENGTH_SHORT
                    ).show()
                }

                return false
            }

            override fun onInfoWindowClick(marker: Marker) {
               val eventArray = marker.tag as? Events

                if (eventArray != null) {
                    Toast.makeText(
                            requireContext(),
                            "${marker.title} has been clicked ${eventArray.time}.",
                            Toast.LENGTH_SHORT
                    ).show()
                }
            }

        })
        val moscow = LatLng(55.7522, 37.6156)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(moscow, 10f))
    }

    private fun getColorMarker(category: String?): Float {
        if (category == "Прогулка") {
            return BitmapDescriptorFactory.HUE_AZURE
        }
        if (category == "Активный отдых") {

            return BitmapDescriptorFactory.HUE_CYAN
        }
        if (category == "Еда") {
            return BitmapDescriptorFactory.HUE_MAGENTA
        }
        if (category == "Культура") {
            return BitmapDescriptorFactory.HUE_RED
        }
        if (category == "Образование") {
            return BitmapDescriptorFactory.HUE_VIOLET
        }
        if (category == "Посиделки") {
            return BitmapDescriptorFactory.HUE_YELLOW
        }
        if (category == "Другое") {
            return BitmapDescriptorFactory.HUE_GREEN
        }

        return BitmapDescriptorFactory.HUE_BLUE
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
