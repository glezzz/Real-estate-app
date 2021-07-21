package com.project.dtttest.model

import android.location.Location
import com.project.dtttest.utils.calculateDistance
import java.io.Serializable

data class HouseResponse(
    val id: Int,
    val image: String,
    val price: Int,
    val bedrooms: Int,
    val bathrooms: Int,
    val size: Int,
    val description: String,
    val zip: String,
    val city: String,
    val latitude: Int,
    val longitude: Int,
    val createdDate: String
) : Serializable {

    //Don't serialize field
    @Transient
    var userLocation: Location? = null
    val distance: Float?
        get() {
            if (userLocation != null) {
                return calculateDistance(
                    userLocation!!.latitude, userLocation!!.longitude,
                    latitude.toDouble(), longitude.toDouble()
                )
            }

            return null
        }

}