package com.example.domain.usecase.feature.hospital

import com.example.domain.model.feature.hospitals.HospitalDetail
import com.example.domain.repository.feature.hospital.HospitalRepository
import javax.inject.Inject

class GetDetailHospitalInfoUseCase @Inject constructor(
    private val repository: HospitalRepository
) {
    suspend operator fun invoke(
        hospitalId: Long,
        userLat: Double,
        userLng: Double
    ): Result<HospitalDetail> {
        return repository.getHospitalDetailInfo(
            hospitalId = hospitalId,
            lat = userLat,
            lng = userLng
        )
    }
}
