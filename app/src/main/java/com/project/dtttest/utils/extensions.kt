package com.project.dtttest.utils

import android.location.Location
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import java.text.DecimalFormat

/**
 * Formats the price number of the house to the desired pattern.
 *
 * @param housePrice the price of the house to be formatted
 * @return           the price of the house formatted
 * @see              DecimalFormat
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
    latUserLocation: Double,
    lonUserLocation: Double,
    latHouseLocation: Double,
    lonHouseLocation: Double
): Float {
    val distance = FloatArray(2)
    Location.distanceBetween(
        latUserLocation, lonUserLocation,
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