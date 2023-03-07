package com.github.aptemkov.locationreminder

import com.google.android.gms.maps.model.LatLng

data class MapResponse(
    val position: LatLng = LatLng(0.0, 0.0),
    val radius: Double,
)