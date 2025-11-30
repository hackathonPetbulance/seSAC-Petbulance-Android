package com.example.data.remote.network.feature.hospital.model

import kotlinx.serialization.Serializable


@Serializable
data class HospitalDetailInfoResponse(
    val hospitalId: Long,
    val name: String,
    val reviewAvg: Double,
    val reviewCount: Int,
    val openNow: Boolean,
    val todayCloseTime: String,
    val distanceKm: Double,
    val phone: String,
    val acceptedAnimals: List<String>,
    val location: HospitalLocation,
    val openHours: List<HospitalOpenHour>
)

@Serializable
data class HospitalLocation(
    val address: String,
    val lat: Double,
    val lng: Double
)

@Serializable
data class HospitalOpenHour(
    val day: String,
    val hours: String
)