package com.example.presentation.screen.hospital

import android.location.Location
import com.example.domain.model.feature.hospitals.HospitalDetail
import com.example.domain.model.feature.review.ReviewList

data class HospitalData(
    val hospital: HospitalDetail,
    val currentUserLocation: Location,
    val reviews: ReviewList?
) {
    companion object {
        fun stub() = HospitalData(
            HospitalDetail.stub,
            Location("default"),
            ReviewList.stub()
        )
    }
}