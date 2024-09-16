package com.example.trips

import android.content.Context
import android.location.Address
import android.location.Geocoder

class MapsFunctions {
    companion object{
        fun getLatLngFromAddress(context: Context, location: String): Address? {
            val coder = Geocoder(context)
            lateinit var address: List<Address>
            try {
                address = coder.getFromLocationName(location, 5) as List<Address>
                if (address == null) {
                    return null
                }

                return address[0]
            } catch (e: Exception) {
                return null
            }
        }
    }
}