package com.project.dtttest.utils

import android.location.Location
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import java.text.DecimalFormat

/**
 * Format price number
 */
fun formatPrice(housePrice: Int): String? {
    return DecimalFormat("#,###").format(housePrice)
}

/**
 * Calculates distance between user location and house coordinates
 */
fun calculateDistance(
    latOwnLocation: Double,
    lonOwnLocation: Double,
    latHouseLocation: Double,
    lonHouseLocation: Double
): Float {
    val distance = FloatArray(2)
    Location.distanceBetween(
        latOwnLocation, lonOwnLocation,
        latHouseLocation, lonHouseLocation, distance
    )
    return DecimalFormat("#.#").format(distance[0] / 1000).toFloat()
}

/**
 * Prepares image to load with Glide
 */
fun loadHouseImage(houseImage: String): GlideUrl {
    val url: String =
        Constants.IMAGE_LOADING_URL + houseImage
    return GlideUrl(
        url,
        LazyHeaders.Builder()
            .addHeader("Access-Key", Constants.ACCESS_KEY)
            .build()
    )
}