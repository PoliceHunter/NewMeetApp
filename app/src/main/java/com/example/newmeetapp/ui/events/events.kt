package com.example.newmeetapp.ui.events

import java.io.Serializable

data class Events  (var name : String? = null, var date : String? = null, var time : String? = null,
                   var gender : String? = null, var category : String? = null,
                   var participants : String? = null, var details : String? = null, var place : Places? = null) : Serializable
{

}

data class Places (var name : String? = null, var id : String? = null,
                   var address : String? = null, var LatLng : pLatLng? = null)
{

}

data class pLatLng(var longitude: Double? = null, var latitude: Double? = null)
{

}