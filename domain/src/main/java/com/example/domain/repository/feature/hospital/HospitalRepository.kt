package com.example.domain.repository.feature.hospital

import com.example.domain.model.feature.hospitals.HospitalDetail
import com.example.domain.model.feature.hospitals.MatchedHospital
import com.example.domain.model.type.HospitalFilterType

interface HospitalRepository {
    suspend fun getMatchingHospitals(
        filter: HospitalFilterType,
        species: String,
        lat: Double,
        lng: Double
    ): Result<List<MatchedHospital>>

    suspend fun getHospitalDetailInfo(
        hospitalId: Long,
        lat: Double,
        lng: Double
    ): Result<HospitalDetail>
}