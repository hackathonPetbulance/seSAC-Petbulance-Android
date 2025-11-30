package com.example.domain.usecase.feature.hospital

import com.example.domain.model.feature.hospitals.MatchedHospital
import com.example.domain.repository.feature.hospital.HospitalRepository
import javax.inject.Inject

class GetHospitalWithFilterUseCase @Inject constructor(
    private val repository: HospitalRepository
) {
    suspend operator fun invoke(filter: HospitalFilterType, species: String, lat: Double, lng: Double): Result<List<MatchedHospital>> {
        return repository.getMatchingHospitals(
            filter = filter,
            species = species,
            lat = lat,
            lng = lng
        )
    }
}