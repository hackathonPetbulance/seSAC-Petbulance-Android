package com.example.domain.usecase.feature.hospital

import com.example.domain.model.feature.hospitals.MatchedHospital
import com.example.domain.repository.feature.hospital.HospitalRepository
import javax.inject.Inject

class GetNearByHospitalUseCase @Inject constructor(
    private val repository: HospitalRepository
) {
    suspend operator fun invoke(lat: Double, lng: Double): Result<List<MatchedHospital>> {
        return repository.getMatchingHospitals(
            filter = HospitalFilterType.DISTANCE,
            species = "",
            lat = lat,
            lng = lng
        )
    }
}