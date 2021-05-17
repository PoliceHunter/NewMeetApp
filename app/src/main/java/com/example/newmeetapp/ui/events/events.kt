package com.example.newmeetapp.ui.events

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.Serializable
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

data class Events  (
        var name : String? = null,
        var date : String? = null,
        var time : String? = null,
        var gender : String? = null,
        var category : String? = null,
        var details : String? = null,
        var id : String? = null,
        var admin : String? = null,
        var place : Places? = null,
        var adminInfo: user? = null,
        var count_participants: String? = null,
        var participantsId: HashMap<String, String>? = null) : Serializable
{
    fun getAdminData(event: Events?)
    {
        val profileReference = FirebaseDatabase.getInstance().getReference("profile/${event!!.admin}")

        profileReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val info = snapshot.getValue(user::class.java)
                    event.adminInfo = info
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

}

data class partList (var id: String? = null) : Serializable
{

}

data class Places (var name : String? = null, var id : String? = null,
                   var address : String? = null, var LatLng : pLatLng? = null) : Serializable
{

}

data class pLatLng(var longitude: Double? = null, var latitude: Double? = null) : Serializable
{

}

data class inRelative(var User: user, var value: Boolean, var idEvent : String) : Serializable

data class user(var firstname : String? = null, var lastname: String? = null, var id: String? = null) : Serializable
{

}