package com.example.presentation.screen.report

import com.example.presentation.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor() : BaseViewModel() {

    private val _state = MutableStateFlow<ReportState>(ReportState.Init)
    val state: StateFlow<ReportState> = _state

    private val _screenState = MutableStateFlow<ReportScreenState>(ReportScreenState.SummaryReport)
    val screenState: StateFlow<ReportScreenState> = _screenState

    private val _eventFlow = MutableSharedFlow<ReportEvent>(replay = 1)
    val eventFlow: SharedFlow<ReportEvent> = _eventFlow

    fun onIntent(intent: ReportIntent) {
        when (intent) {
            is ReportIntent.SomeIntentWithoutParams -> {
                //do sth
            }

            is ReportIntent.ScreenTransition -> {
                _screenState.value = intent.tobe
            }
        }
    }

    init {
        launch {

        }
    }
}
