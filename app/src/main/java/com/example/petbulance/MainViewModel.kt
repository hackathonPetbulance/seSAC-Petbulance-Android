package com.example.petbulance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.type.AppTheme
import com.example.domain.repository.nonfeature.preference.GetThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getThemeUseCase: GetThemeUseCase
) : ViewModel() {
    val appTheme: StateFlow<AppTheme> = getThemeUseCase()
        .map { result ->
            result.getOrDefault(AppTheme.DEVICE)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AppTheme.DEVICE
        )
}