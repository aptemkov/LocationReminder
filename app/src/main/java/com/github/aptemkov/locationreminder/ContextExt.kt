package com.github.aptemkov.locationreminder

import android.Manifest.permission.*
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat


fun Context.hasLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
}

fun Context.hasVibrationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(this, VIBRATE) == PackageManager.PERMISSION_GRANTED
}