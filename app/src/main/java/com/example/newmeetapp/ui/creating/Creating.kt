package com.example.newmeetapp.ui.creating

import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.newmeetapp.R
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.GoogleMap
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.creating_fragment.*

class Creating : Fragment() {

    private lateinit var mMap: GoogleMap
    private lateinit var databaseReference: DatabaseReference
    private lateinit var etPlace: Place
    var database: FirebaseDatabase? = null
    var radioTime: RadioButton? = null
    var radioSex: RadioButton? = null
    var etEventName : EditText? = null

    companion object {
        fun newInstance() = Creating()

    }


    private lateinit var viewModel: CreatingViewModel

    // Отображает creating_fragment.xml
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.creating_fragment, container, false)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CreatingViewModel::class.java)
        // TODO: Use the ViewModel

        //DB connect
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("places")
        val apiKey = getString(R.string.api_key)

        // 64-86 autocomplete
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), apiKey)
        }
        val autocompleteFragment =
                childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                        as AutocompleteSupportFragment
        autocompleteFragment.setCountries("RU")
        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS))
        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                etPlace = place
                Log.i("autocomplete", "Place: ${place.name}, ${place.id},  ${place.latLng}, ${place.address}")
            }
            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i( "autocomplete", "An error occurred: $status")
            }
        })

        textInputEditTextEventName.addTextChangedListener {
            etEventName = activity?.findViewById(R.id.textInputEditTextEventName) as EditText
            val etTextEventName = childFragmentManager.findFragmentById(R.id.textInputEditTextEventName)
        }

        timeEventGroup.setOnCheckedChangeListener(
                RadioGroup.OnCheckedChangeListener{
                    group, checkedId ->
                    radioTime = activity?.findViewById(checkedId)
                }
        )

        radioSexGroup.setOnCheckedChangeListener(
                RadioGroup.OnCheckedChangeListener{
                    group, checkedId ->
                    radioSex = activity?.findViewById(checkedId)
                }
        )






        bt_next.setOnClickListener{
            Log.d("Args wich save to db", "time - ${radioTime?.text}, sex - ${radioSex?.text}, name - ${etEventName?.text} . ${textInputEditTextEventName.text}" +
                    "place - ${etPlace.address}")
        }
    // возможно, нужно очищать поле или при мапинге в бд смотреть на состояние свитч
        view?.let { switchParticipantsCountVisibility(it) }

    }



    private fun savePlaceToDB(place: Place) {
        // for pathString for place need unical id
        val currentUserDb = databaseReference.child(place.id.toString())
        currentUserDb.child("name").setValue(place.name)
        currentUserDb.child("id").setValue(place.id)
        currentUserDb.child("LatLng").setValue(place.latLng)
        currentUserDb.child("address").setValue(place.address.toString())
    }


//    fun goToInvitations (view: View) {
//        val btNext = view.findViewById<Button>(R.id.bt_next)
//        btNext.setOnClickListener {
//                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, InvitingFragment()).addToBackStack(null).commit()
//            }
//    }




    private fun switchParticipantsCountVisibility (view: View) {
        view.findViewById<Switch>(R.id.switchEventParticipantsCount).setOnCheckedChangeListener { _, isChecked ->
            view.findViewById<TextInputLayout>(R.id.textInputLayoutEventParticipantsCount).visibility = if (isChecked) View.VISIBLE
            else View.INVISIBLE
        }
    }

}