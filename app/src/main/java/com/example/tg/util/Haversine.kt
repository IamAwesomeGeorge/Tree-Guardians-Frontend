package com.example.tg.util

class Haversine(private val lat: Double, private val lon: Double) {

    companion object {
        const val earthRadius: Double = 6372.8;
    }

    fun getDistance(destination: Haversine): Double {
        val dLat = Math.toRadians(destination.lat - this.lat)
        val dLon = Math.toRadians(destination.lon - this.lon)
        val originLat = Math.toRadians(this.lat)
        val destinationLat = Math.toRadians(destination.lat)

        val a = Math.pow(Math.sin(dLat / 2), 2.toDouble()) + Math.pow(Math.sin(dLon / 2), 2.toDouble()) * Math.cos(originLat) * Math.cos(destinationLat);
        val c = 2 * Math.asin(Math.sqrt(a));

        return earthRadius * c;
    }
}