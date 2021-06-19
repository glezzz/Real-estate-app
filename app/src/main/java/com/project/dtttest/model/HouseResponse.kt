package com.project.dtttest.model

data class HouseResponse(
    val id: Int,
    val image: String,
    val price: Int,
    val bedrooms: Int,
    val bathrooms: Int,
    val size: Int,
    val description: String,
    val zip: String,
    val latitude: Int,
    val longitude: Int,
    val createdDate: String
) {
}