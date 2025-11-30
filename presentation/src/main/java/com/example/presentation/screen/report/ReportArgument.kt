package com.example.presentation.screen.report

import com.example.presentation.screen.diagnosis.DiagnosisIntent
import com.example.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class ReportArgument(
    val reportIntent: (ReportIntent) -> Unit,
    val diagnosisIntent: (DiagnosisIntent) -> Unit,
    val state: ReportState,
    val screenState: ReportScreenState,
    val event: SharedFlow<ReportEvent>
)

sealed class ReportState {
    data object Init : ReportState()
    data object OnProgress : ReportState()
}

sealed class ReportScreenState {
    data object SummaryReport : ReportScreenState()
    data object HospitalMatching : ReportScreenState()
    data object FirstAidGuide : ReportScreenState()
}

sealed class ReportIntent {
    data class ScreenTransition(val tobe: ReportScreenState) : ReportIntent()
    data object SomeIntentWithoutParams : ReportIntent()
}

sealed class ReportEvent {
    sealed class DataFetch : ReportEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?
        ) : DataFetch(), ErrorEvent
    }
}