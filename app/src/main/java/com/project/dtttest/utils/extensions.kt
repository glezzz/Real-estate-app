package com.project.dtttest.utils

import android.location.Location
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import java.text.DecimalFormat

/**
 * Format price number
 */
const val pricePattern = "\$###,###.###"
fun formatPrice(housePrice: Int): String? {
    return DecimalFormat(pricePattern).format(housePrice)
}
/**
 * Format distance
 */
const val distancePattern = "###.# km"
fun formatDistance(distance: Float): String {
    val formattedDistance = distance / 1000
    return DecimalFormat(distancePattern).format(formattedDistance)
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
    return distance[0]
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