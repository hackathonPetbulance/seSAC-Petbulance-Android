package com.example.presentation.screen.home

import com.example.domain.model.feature.hospitals.MatchedHospital
import com.example.domain.model.feature.review.HospitalReview

data class HomeData(
    val matchedHospitals: List<MatchedHospital>,
    val hospitalReviews: List<HospitalReview>
) {
    companion object {
        fun stub() = HomeData(
            matchedHospitals = listOf(MatchedHospital.stub()),
            hospitalReviews = listOf(
                HospitalReview.stub(),
                HospitalReview.stub().copy(reviewId = 2L)
            )
        )
    }
}