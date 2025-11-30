package com.example.data.repository.feature.diagnosis

import androidx.core.net.toUri
import com.example.data.remote.network.feature.diagnosis.DiagnosisApi
import com.example.data.remote.network.feature.diagnosis.model.DiagnosisResponse
import com.example.data.utils.safeApiCall
import com.example.domain.model.feature.diagnosis.AiDiagnosis
import com.example.domain.repository.feature.diagnosis.DiagnosisRepository
import javax.inject.Inject

class DiagnosisRepositoryImpl @Inject constructor(
    private val api: DiagnosisApi
) : DiagnosisRepository {

    override suspend fun requestDiagnosis(
        images: List<String>,
        animalType: String,
        symptom: String,
        onUpload: (bytesSent: Long, totalBytes: Long) -> Unit
    ): Result<AiDiagnosis> {
        return safeApiCall<DiagnosisResponse> {
            api.requestDiagnosis(
                images = images.map { it.toUri() },
                animalType = animalType,
                symptom = symptom,
                onUpload = onUpload
            )
        }.map { it.toDomain() }
    }
}