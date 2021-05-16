package com.example.newmeetapp.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newmeetapp.R
import com.example.newmeetapp.ui.eventInfo.EventInfo
import com.example.newmeetapp.ui.events.Events
import com.example.newmeetapp.ui.events.user
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.profile_fragment.*

class Profile : Fragment() {

    companion object {
        fun newInstance() = Profile()
    }

    lateinit var auth: FirebaseAuth
    private lateinit var viewModel: ProfileViewModel
    private lateinit var databaseReference: DatabaseReference
    private var firstname : String? = null
    private var lastname : String? = null
    private var city : String? = null
    private var about : String? = null
    private var birthday : String? = null
    private var phone : String? = null
    private var telegram : String? = null
    private var instagram : String? = null
    private var vk : String? = null
    var database: FirebaseDatabase? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        auth = FirebaseAuth.getInstance()
        getUserData()
        //nameId.text = "$firstname $lastname"

        // TODO: Use the ViewModel
    }

    @SuppressLint("SetTextI18n")
    private fun getUserData(){
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")
        val currentUser = auth.currentUser
        val currentUserDb = databaseReference.child(currentUser?.uid!!)

        currentUserDb.addValueEventListener(object: ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot)
            {
                firstname = snapshot.child("firstname").value.toString()
                lastname = snapshot.child("lastname").value.toString()
                nameId.text = "$firstname $lastname"
                CityId.text = snapshot.child("city").value.toString()
                AboutId.text = snapshot.child("about").value.toString()
                BirthDayId.text = snapshot.child("birthday").value.toString()
                PhoneId.text = snapshot.child("phone").value.toString()
                TelegramId.text = snapshot.child("telegram").value.toString()
                InstagramId.text = snapshot.child("instagram").value?.toString()
                VkId.text = snapshot.child("vk").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Error access to db", "All bad")
            }
        })

    }

}