package com.example.data.repository.feature.hospital

import android.util.Log
import com.example.data.remote.network.feature.hospital.HospitalApi
import com.example.data.remote.network.feature.hospital.model.HospitalDetailInfoResponse
import com.example.data.remote.network.feature.hospital.model.MatchingHospitalResponse
import com.example.data.repository.feature.diagnosis.toDomain
import com.example.data.utils.safeApiCall
import com.example.domain.model.feature.hospitals.HospitalDetail
import com.example.domain.model.feature.hospitals.MatchedHospital
import com.example.domain.model.type.HospitalFilterType
import com.example.domain.repository.feature.hospital.HospitalRepository
import javax.inject.Inject

class HospitalRepositoryImpl @Inject constructor(
    private val hospitalApi: HospitalApi
) : HospitalRepository {

    override suspend fun getMatchingHospitals(
        filter: HospitalFilterType,
        species: String?,
        lat: Double,
        lng: Double
    ): Result<List<MatchedHospital>> {
        Log.d(
            "siria22",
            "Repository Impl: ${filter}, ${species?.toAnimalSpecies()}, ${lat}, ${lng}"
        )
        return safeApiCall<List<MatchingHospitalResponse>> {
            hospitalApi.getMatchingHospitals(
                filter = filter,
                species = species?.toAnimalSpecies(),
                lat = lat,
                lng = lng
            )
        }.map {responseList -> responseList.map { it.toDomain() }  }
    }

    override suspend fun getHospitalDetailInfo(
        hospitalId: Long,
        lat: Double,
        lng: Double
    ): Result<HospitalDetail> {
        return safeApiCall<HospitalDetailInfoResponse> {
            hospitalApi.getHospitalDetailInfo(
                hospitalId,
                lat,
                lng
            )
        }.map { it.toDomain()}
    }
}

private fun String.toAnimalSpecies(): String? {
    return when (this) {
        "조류" -> "PARROT"
        "파충류" -> "GECKO"
        "소형 포유류" -> "HAMSTER"
        else -> null
    }
}