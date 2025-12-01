package com.example.presentation.screen.search

import android.location.Location
import com.example.domain.model.feature.hospitals.HospitalDetail

data class SearchData(
    val hospital: HospitalDetail?,
    val currentUserLocation: Location
) {
    companion object {
        fun empty() = SearchData(
            hospital = null,
            currentUserLocation = Location("default").apply {
                latitude = 37.5665
                longitude = 126.9780
            }
        )
    }
}