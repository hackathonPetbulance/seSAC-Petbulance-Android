package com.example.presentation.screen.diagnosis

import android.net.Uri
import com.example.domain.model.type.HospitalFilterType
import com.example.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class DiagnosisArgument(
    val intent: (DiagnosisIntent) -> Unit,
    val state: DiagnosisState,
    val event: SharedFlow<DiagnosisEvent>
)

sealed class DiagnosisState {
    data object Init : DiagnosisState()
    data object OnProgress : DiagnosisState()
}

sealed class DiagnosisIntent {
    data class UpdateAnimalSpecies(val species: String) : DiagnosisIntent()
    data class UpdateDescription(val description: String) : DiagnosisIntent()
    data class UpdateImageUris(val uri: Uri?, val index: Int) : DiagnosisIntent()
    data class RequestDiagnosis(val onUpload: (Long, Long) -> Unit) : DiagnosisIntent()
    data class MatchHospitalByFilter(val filter: HospitalFilterType) : DiagnosisIntent()
}


sealed class DiagnosisEvent {
    data object RequestSuccess : DiagnosisEvent()
    sealed class DataFetch : DiagnosisEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?
        ) : DataFetch(), ErrorEvent
    }
}