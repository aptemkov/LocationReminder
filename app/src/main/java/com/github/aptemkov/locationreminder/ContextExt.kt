package com.github.aptemkov.locationreminder

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat


fun Context.hasLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
}