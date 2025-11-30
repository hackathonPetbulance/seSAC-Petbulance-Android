package com.example.data.repository.feature.hospital

import com.example.domain.model.feature.hospitals.HospitalDetail
import com.example.domain.model.feature.hospitals.MatchedHospital
import com.example.domain.repository.feature.hospital.HospitalRepository
import com.example.domain.usecase.feature.hospital.HospitalFilterType
import javax.inject.Inject

class MockHospitalRepository @Inject constructor() : HospitalRepository {

    override suspend fun getMatchingHospitals(
        filter: HospitalFilterType,
        species: String,
        lat: Double,
        lng: Double
    ): Result<List<MatchedHospital>> {
        return Result.success(
            listOf(
                MatchedHospital.stub(),
                MatchedHospital.stub().copy(
                    hospitalId = 10L,
                    name = "병원1",
                    isOpenNow = false,
                    distanceKm = 1.0,
                    treatableAnimals = listOf("토끼"),
                    phone = "02-124-5678",
                    thumbnailUrl = "fffssss",
                    todayCloseTime = "21:00"
                )
            )
        )
    }

    override suspend fun getHospitalDetailInfo(
        hospitalId: Long,
        lat: Double,
        lng: Double
    ): Result<HospitalDetail> {
        return Result.success(
            HospitalDetail.stub
        )
    }
}