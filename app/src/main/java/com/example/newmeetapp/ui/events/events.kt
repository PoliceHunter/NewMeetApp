package com.example.newmeetapp.ui.events

import android.net.Uri
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
        var count_participants: String? = null) : Serializable
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

    fun getUserData(event: Events?, id: String?): user? {
        val profileReference = FirebaseDatabase.getInstance().getReference("profile/$id")

        var info: user ? = null
        profileReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                 info = snapshot.getValue(user::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        return info

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

data class user(var firstname : String? = null,
                var lastname: String? = null,
                var id: String? = null,
                var about: String? = null,
                var birthday : String? = null,
                var city : String? = null,
                var phone : String? = null,
                var telegram: String? = null,
                var vk: String? = null,
                var instagram : String? = null,
                var uri: String? = null) : Serializable
{

}

data class notiFi(var event : Events? = null)