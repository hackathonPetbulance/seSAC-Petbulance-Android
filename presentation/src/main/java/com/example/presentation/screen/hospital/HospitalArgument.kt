package com.example.presentation.screen.hospital

import com.example.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class HospitalArgument(
    val intent: (HospitalIntent) -> Unit,
    val state: HospitalState,
    val event: SharedFlow<HospitalEvent>
)

sealed class HospitalState {
    data object Init : HospitalState()
    data object OnProgress : HospitalState()
}

sealed class HospitalIntent {
    data class SomeIntentWithParams(val param: String) : HospitalIntent()
    data object SomeIntentWithoutParams : HospitalIntent()
}

sealed class HospitalEvent {
    sealed class PermissionCheck: HospitalEvent() {
        data class LackOfPermission(
            override val userMessage: String = "앱 권한이 부족하여 정확한 위치를 표시할 수 없어요.",
            override val exceptionMessage: String?
        ) : PermissionCheck(), ErrorEvent
        data class NoPermissionGranted(
            override val userMessage: String = "앱 권한이 부족하여 정확한 위치를 표시할 수 없어요.",
            override val exceptionMessage: String?
        ) : PermissionCheck(), ErrorEvent
    }
    sealed class DataFetch : HospitalEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?
        ) : DataFetch(), ErrorEvent
    }
}