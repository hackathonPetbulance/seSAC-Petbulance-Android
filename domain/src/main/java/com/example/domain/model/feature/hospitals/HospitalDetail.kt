package com.example.domain.model.feature.hospitals

import com.example.domain.model.type.AnimalSpecies

data class HospitalDetail(
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

    val openHours: List<OpenHours>,
) {
    companion object {
        val stub = HospitalDetail(
            hospitalId = 12L,
            name = "서울 특수동물 의료센터",
            reviewAvg = 4.5,
            reviewCount = 12,
            openNow = true,
            todayCloseTime = "20:00",
            distanceKm = 1.2,
            phone = "02-123-4567",
            acceptedAnimals = listOf("소형 포유류"),
            location = HospitalLocation(
                address = "서울특별시 중구 남대문로 120",
                lat = 37.5666,
                lng = 126.9780
            ),
            openHours = listOf(
                OpenHours(day = "MON", hours = "09:00 - 18:00"),
                OpenHours(day = "TUE", hours = "09:00 - 18:00"),
                OpenHours(day = "WED", hours = "09:00 - 18:00"),
                OpenHours(day = "THU", hours = "09:00 - 18:00"),
                OpenHours(day = "FRI", hours = "09:00 - 18:00"),
                OpenHours(day = "SAT", hours = "09:00 - 18:00"),
                OpenHours(day = "SUN", hours = "09:00 - 18:00")
            )
        )
    }
}

data class OpenHours(
    val day: String,
    val hours: String
)

data class HospitalLocation(
    val address: String,
    val lat: Double,
    val lng: Double,
)