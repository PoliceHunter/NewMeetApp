package com.example.newmeetapp.ui.events

import android.renderscript.Sampler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.snapshot.ValueIndex
import java.io.Serializable
import java.security.Key
import java.util.*
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


data class user(var firstname : String? = null, var lastname: String? = null, var id: String? = null) : Serializable
{

}