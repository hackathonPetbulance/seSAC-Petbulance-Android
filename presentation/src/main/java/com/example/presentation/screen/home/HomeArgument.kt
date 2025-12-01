package com.example.presentation.screen.home

import com.example.domain.model.type.AnimalCategory
import com.example.domain.model.type.AnimalSpecies
import com.example.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class HomeArgument(
    val intent: (HomeIntent) -> Unit,
    val state: HomeState,
    val event: SharedFlow<HomeEvent>
)

sealed class HomeState {
    data object Init : HomeState()
    data object OnProgress : HomeState()
}

sealed class HomeIntent {
    data object GetNearByHospital : HomeIntent()
    data class ChangeReviewCategory(val animalCategory: AnimalCategory) : HomeIntent()
}

sealed class HomeEvent {
    sealed class DataFetch : HomeEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?
        ) : DataFetch(), ErrorEvent
    }
}