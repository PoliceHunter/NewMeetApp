package com.example.newmeetapp.ui.creating

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.newmeetapp.R
import com.example.newmeetapp.ui.inviting.InvitingFragment
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.GoogleMap
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.creating_fragment.*
import java.util.*
import kotlin.random.Random

class Creating : Fragment() {

    private lateinit var mMap: GoogleMap
    private lateinit var databaseReference: DatabaseReference
    var etPlace: Place? = null
    var database: FirebaseDatabase? = null
    var radioTime: RadioButton? = null
    var radioSex: RadioButton? = null
    var etEventName : EditText? = null
    var radioCategory : RadioButton? = null
    private lateinit var etUnicalID : UUID
    val MIN_LENTH_NAME = 3

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
        databaseReference = database?.reference!!.child("events")
        val apiKey = getString(R.string.api_key2)

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
            Log.i("etTextEventName= ", "$etTextEventName")
        }

        radioGroupEventCategory.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener{
                group, checkedId ->
                radioCategory = activity?.findViewById(checkedId)
            }
        )

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

        //Calendar
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        eventDate.setOnClickListener {
            val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                //set to TextView
                eventDate.setText(""+ mDay + "." + (mMonth + 1) + "." + mYear)
            }, year, month, day)
            dpd.show()
            dpd.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#2CAFF1"))
            dpd.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#2CAFF1"))
        }

        view?.let { switchParticipantsCountVisibility(it) }



        bt_next.setOnClickListener {
            if (validation()) {

                etUnicalID = UUID.randomUUID()
                Log.d("Args which save to db", "time - ${radioTime?.text}, sex - ${radioSex?.text}, name - ${etEventName?.text}dd")
                val currentUserDb = databaseReference.child(etUnicalID.toString())
                currentUserDb.child("name").setValue(etEventName?.text.toString())
                currentUserDb.child("time").setValue(radioTime?.text.toString())
                currentUserDb.child("gender").setValue(radioSex?.text.toString())
                currentUserDb.child("date").setValue(eventDate?.text.toString())
                currentUserDb.child("category").setValue(radioCategory?.text.toString())
                currentUserDb.child("participants").setValue(textInputEditTextEventParticipantsCount.text.toString())
                currentUserDb.child("details").setValue(editText_eventDetails?.text.toString())
                etPlace?.let { it1 -> savePlaceToDB(it1) }

            }
            goToInvitations(it)
        }
    }

    private fun validation() : Boolean
    {
        if (!timeEventGroup.isSelected)
        {
            timeEventGroup.check(R.id.RadioBtTimeAny)
            radioTime = activity?.findViewById(R.id.RadioBtTimeAny)
        }
        if (!radioGroupEventCategory.isSelected)
        {
            radioGroupEventCategory.check(R.id.RadioBtCategory_Other)
            radioCategory = activity?.findViewById(R.id.RadioBtCategory_Other)
        }

        if (!radioSexGroup.isSelected)
        {
            radioSexGroup.check(R.id.RadioBtGenderAny)
            radioSex = activity?.findViewById(R.id.RadioBtGenderAny)
        }
        if (textInputEditTextEventName.text?.isEmpty()!!)
        {
            textInputEditTextEventName.error = "Please enter event name"
            return false
        }
        if (textInputEditTextEventName.text?.length!! < MIN_LENTH_NAME)
        {
            textInputEditTextEventName.error = "Event name must be more than $MIN_LENTH_NAME characters"
            return false
        }
        if (eventDate.text.isEmpty())
        {
            eventDate.error = "Please enter date"
            return false
        }
        if (switchEventParticipantsCount.isChecked)
        {
            if (textInputEditTextEventParticipantsCount.text?.isEmpty()!!)
            {
                Log.i("Switch","${textInputEditTextEventParticipantsCount.text} and it-s worked")
                textInputEditTextEventParticipantsCount.error = "Please enter count participants"
                return false
            }
        }
        return true
    }


    private fun savePlaceToDB(place: Place) {
        // for pathString for place need unical id
        val currentUserDb = databaseReference.child("$etUnicalID/place")
        currentUserDb.child("name").setValue(place.name)
        currentUserDb.child("id").setValue(place.id)
        currentUserDb.child("LatLng").setValue(place.latLng)
        currentUserDb.child("address").setValue(place.address.toString())
    }

    fun goToInvitations (view: View) {
        val btNext = view.findViewById<Button>(R.id.bt_next)
        btNext.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, InvitingFragment()).addToBackStack(null).commit()
            }
    }

    private fun switchParticipantsCountVisibility (view: View) {
        view.findViewById<Switch>(R.id.switchEventParticipantsCount).setOnCheckedChangeListener { _, isChecked ->
            view.findViewById<TextInputLayout>(R.id.textInputLayoutEventParticipantsCount).visibility = if (isChecked)
            {
                textInputLayoutEventParticipantsCount.requestFocus()
                View.VISIBLE
            }
            else
            {
                textInputEditTextEventParticipantsCount.text = null
                View.INVISIBLE
            }
        }
    }
}