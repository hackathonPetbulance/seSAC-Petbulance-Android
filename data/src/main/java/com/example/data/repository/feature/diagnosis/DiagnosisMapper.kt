package com.example.data.repository.feature.diagnosis

import com.example.data.remote.network.feature.diagnosis.model.DiagnosisResponse
import com.example.data.remote.network.feature.diagnosis.model.FirstAidGuideContentResponse
import com.example.data.remote.network.feature.hospital.model.HospitalDetailInfoResponse
import com.example.domain.model.feature.diagnosis.AiDiagnosis
import com.example.domain.model.feature.diagnosis.FirstAidGuide
import com.example.domain.model.feature.diagnosis.FirstAidGuideContent
import com.example.domain.model.feature.hospitals.HospitalDetail
import com.example.domain.model.feature.hospitals.HospitalLocation
import com.example.domain.model.feature.hospitals.OpenHours
import com.example.domain.model.type.EmergencyLevel

fun DiagnosisResponse.toDomain(): AiDiagnosis {
    val data = this.data
    return AiDiagnosis(
        animalType = data.animalType,
        emergencyLevel = when (data.emergencyLevel.uppercase()) {
            "HIGH" -> EmergencyLevel.HIGH
            "MIDDLE" -> EmergencyLevel.MIDDLE
            "LOW" -> EmergencyLevel.LOW
            else -> EmergencyLevel.MIDDLE
        },
        detectedSymptoms = data.detectedSymptoms,
        suspectedDisease = data.suspectedDisease,
        recommendedActions = data.recommendedActions,
        firstAidGuide = FirstAidGuide(
            totalSteps = data.totalSteps,
            steps = data.steps.map { it.toDomain() }
        ),
        confidence = data.confidence
    )
}

fun FirstAidGuideContentResponse.toDomain() = FirstAidGuideContent(
    description = this.description,
    warning = this.warning
)

fun HospitalDetailInfoResponse.toDomain() = HospitalDetail(
    hospitalId = this.hospitalId,
    name = this.name,
    reviewAvg = this.reviewAvg,
    reviewCount = this.reviewCount,
    openNow = this.openNow,
    todayCloseTime = this.todayCloseTime,
    distanceKm = this.distanceKm,
    phone = this.phone,
    acceptedAnimals = this.acceptedAnimals,
    location = HospitalLocation(
        address = this.location.address,
        lat = this.location.lat,
        lng = this.location.lng
    ),
    openHours = this.openHours.map { OpenHours(it.day, it.hours) }
)