package com.example.data.repository.feature.hospital

import com.example.data.remote.network.feature.hospital.HospitalApi
import com.example.data.remote.network.feature.hospital.model.HospitalDetailInfoResponse
import com.example.data.remote.network.feature.hospital.model.MatchingHospitalsResponse
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
        species: String,
        lat: Double,
        lng: Double
    ): Result<List<MatchedHospital>> {
        return safeApiCall<MatchingHospitalsResponse> {
            hospitalApi.getMatchingHospitals(
                filter = filter,
                species = species,
                lat = lat,
                lng = lng
            )
        }.map { it.toHospitalCardList() }
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