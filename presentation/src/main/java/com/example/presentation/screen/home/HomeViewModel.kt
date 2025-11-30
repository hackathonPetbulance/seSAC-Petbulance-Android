package com.example.presentation.screen.home

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import com.example.domain.model.feature.hospitals.MatchedHospital
import com.example.domain.model.feature.reviews.HospitalReview
import com.example.domain.usecase.feature.hospital.GetNearByHospitalUseCase
import com.example.presentation.utils.BaseViewModel
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getNearByHospitalUseCase: GetNearByHospitalUseCase,
    application: Application
) : BaseViewModel() {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    private val _state = MutableStateFlow<HomeState>(HomeState.Init)
    val state: StateFlow<HomeState> = _state

    private val _eventFlow = MutableSharedFlow<HomeEvent>()
    val eventFlow: SharedFlow<HomeEvent> = _eventFlow
    private val _Matched_hospitalCards = MutableStateFlow(emptyList<MatchedHospital>())
    val matchedHospitalCards: StateFlow<List<MatchedHospital>> = _Matched_hospitalCards

    private val _hospitalReviews = MutableStateFlow(emptyList<HospitalReview>())
    val hospitalReviews: StateFlow<List<HospitalReview>> = _hospitalReviews


    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.GetNearByHospital -> {
                launch { getNearByHospital() }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getNearByHospital() {
        _state.value = HomeState.OnProgress

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                launch {
                    runCatching {
                        getNearByHospitalUseCase(location.latitude, location.longitude)
                    }.onSuccess { result ->
                        Log.d(
                            "siria22",
                            "Request Success from : ${location.latitude},${location.longitude}"
                        )
                        _Matched_hospitalCards.value = result.getOrThrow()
                    }.onFailure { ex ->
                        _eventFlow.emit(
                            HomeEvent.DataFetch.Error(
                                userMessage = "근처 병원 조회에 실패했어요.",
                                exceptionMessage = ex.message
                            )
                        )
                    }
                }
            } else {
                launch {
                    _eventFlow.emit(
                        HomeEvent.DataFetch.Error(
                            userMessage = "위치 정보를 가져올 수 없습니다.",
                            exceptionMessage = "Location is null"
                        )
                    )
                }
            }
        }.addOnFailureListener { ex ->
            launch {
                _eventFlow.emit(
                    HomeEvent.DataFetch.Error(
                        userMessage = "위치 정보 조회에 실패했습니다.",
                        exceptionMessage = ex.message
                    )
                )
            }
        }
        _state.value = HomeState.Init
    }
}