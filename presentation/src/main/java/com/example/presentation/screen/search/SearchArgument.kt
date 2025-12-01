package com.example.presentation.screen.search

import com.example.presentation.screen.hospital.HospitalEvent
import com.example.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class SearchArgument(
val intent: (SearchIntent) -> Unit,
val state:SearchState,
val event: SharedFlow<SearchEvent>
)

sealed class SearchState {
    data object Init : SearchState()
    data object OnProgress :  SearchState()
}

sealed class SearchIntent { }

sealed class SearchEvent {

    sealed class PermissionCheck : SearchEvent() {
        data class LackOfPermission(
            override val userMessage: String = "앱 권한이 부족하여 정확한 위치를 표시할 수 없어요.",
            override val exceptionMessage: String?
        ) : PermissionCheck(), ErrorEvent

        data class NoPermissionGranted(
            override val userMessage: String = "앱 권한이 부족하여 정확한 위치를 표시할 수 없어요.",
            override val exceptionMessage: String?
        ) : PermissionCheck(), ErrorEvent
    }
    sealed class DataFetch : SearchEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?
        ) : DataFetch(), ErrorEvent
    }
}