package com.example.domain.model.feature.hospitals

import com.example.domain.model.type.AnimalSpecies

data class Hospital(
    val hospitalId: Long,
    val name: String,
    val lat: Double,
    val lng: Double,
    val distanceMeters: Double,
    val phone: String,
    val types: List<AnimalSpecies>,
    val isOpenNow: Boolean,
    val openHours: String,
    val thumbnailUrl: String,
    val rating: Double,
    val reviewCount: Long
) {
    companion object {
        fun stub() = Hospital(
            hospitalId = 1,
            name = "병원 이름",
            lat = 0.0,
            lng = 0.0,
            distanceMeters = 0.0,
            phone = "010-1234-5678",
            types = listOf(AnimalSpecies.HAMSTER, AnimalSpecies.HAMSTER),
            isOpenNow = true,
            openHours = "09:00 - 18:00",
            thumbnailUrl = "https://example.com/thumbnail.jpg",
            rating = 4.5,
            reviewCount = 100
        )
    }
}