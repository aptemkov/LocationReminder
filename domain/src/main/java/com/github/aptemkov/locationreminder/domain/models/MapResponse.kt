package com.github.aptemkov.locationreminder.domain.models

data class MapResponse(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val radius: Double,
)