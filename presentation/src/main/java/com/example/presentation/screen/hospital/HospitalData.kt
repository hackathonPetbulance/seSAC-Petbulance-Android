package com.example.presentation.screen.hospital

import com.example.domain.model.feature.hospitals.HospitalDetail

data class HospitalData(
    val hospital: HospitalDetail
) {
    companion object {
        fun empty() = HospitalDetail.stub
    }
}