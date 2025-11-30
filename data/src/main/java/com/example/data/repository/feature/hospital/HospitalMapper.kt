package com.example.data.repository.feature.hospital

import com.example.data.remote.network.feature.hospital.model.MatchingHospitals
import com.example.data.remote.network.feature.hospital.model.MatchingHospitalsResponse
import com.example.domain.model.feature.hospitals.MatchedHospital

fun MatchingHospitalsResponse.toHospitalCardList(): List<MatchedHospital> {
    return this.hospitals.map { it.toDomain() }
}

fun MatchingHospitals.toDomain(): MatchedHospital {
    return MatchedHospital(
        hospitalId = this.hospitalId,
        thumbnailUrl = this.thumbnailUrl,
        name = this.name,
        isOpenNow = this.isOpenNow,
        distanceKm = this.distanceKm,
        todayCloseTime = this.todayCloseTime,
        treatableAnimals = this.treatableAnimals,
        phone = this.phone
    )
}