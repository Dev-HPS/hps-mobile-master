package com.hastaprimasolusi.rana.helper

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import java.util.*

class LocationHelper(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = 10000 // 10 seconds
        fastestInterval = 5000 // 5 seconds
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY // For high accuracy
    }

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult.locations.firstOrNull()?.let { location ->
                // You have the location
                handleLocationUpdate(location)
            }
        }
    }

    // Start location updates (reusable method)
    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            // Handle permission request if not granted yet
            Toast.makeText(context, "Permission required to access location", Toast.LENGTH_SHORT).show()
            return
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    // Stop location updates (reusable method)
    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    // Get the last known location (single-use method)
    @SuppressLint("MissingPermission")
    fun getLastKnownLocation(callback: (Location?) -> Unit) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            callback(location)
        }
    }

    // Handle location update (you can extend this method)
    private fun handleLocationUpdate(location: Location) {
        val lat = location.latitude
        val lng = location.longitude

        // Get address from location
        val address = getAddressFromLocation(lat, lng)

        // You can now use lat, lng, and address as needed
        Toast.makeText(context, "Location: $lat, $lng, Address: $address", Toast.LENGTH_SHORT).show()
    }
    fun getAddressFromLocation(lat: Double, lng: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        return try {
            // Convert the MutableList to an immutable List
            val addresses: List<Address> = geocoder.getFromLocation(lat, lng, 1)?.toList() ?: emptyList()

            // Safely get the address line from the first address or return "Unknown location"
            addresses.firstOrNull()?.getAddressLine(0) ?: "Unknown location"
        } catch (e: Exception) {
            "Address not found"
        }
    }
}
