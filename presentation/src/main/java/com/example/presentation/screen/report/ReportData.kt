package com.example.presentation.screen.report

import com.example.domain.model.feature.diagnosis.AiDiagnosis
import com.example.domain.model.feature.hospitals.MatchedHospital

data class ReportData(
    val aiDiagnosis: AiDiagnosis,
    val userLocation: String,
    val matchedHospitals: List<MatchedHospital>

    ) {
    companion object {
        fun empty() = ReportData(
            AiDiagnosis.empty(),
            userLocation = "서울 마포구",
            matchedHospitals = emptyList()
        )
    }
}