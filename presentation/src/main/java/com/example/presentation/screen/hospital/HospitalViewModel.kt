package com.example.presentation.screen.hospital

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.example.domain.model.feature.hospitals.HospitalDetail
import com.example.domain.usecase.feature.hospital.GetDetailHospitalInfoUseCase
import com.example.presentation.utils.BaseViewModel
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class HospitalViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getDetailHospitalInfoUseCase: GetDetailHospitalInfoUseCase,
    application: Application
) : BaseViewModel() {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    private val _state = MutableStateFlow<HospitalState>(HospitalState.Init)
    val state: StateFlow<HospitalState> = _state

    private val _eventFlow = MutableSharedFlow<HospitalEvent>(replay = 1)
    val eventFlow: SharedFlow<HospitalEvent> = _eventFlow

    private val _hospital = MutableStateFlow<HospitalDetail?>(null)
    val hospital: StateFlow<HospitalDetail?> = _hospital

    private val hospitalId: Long = savedStateHandle["hospitalId"] ?: 0L

    fun onIntent(intent: HospitalIntent) {
        when (intent) {
            is HospitalIntent.SomeIntentWithoutParams -> {
                //do sth
            }

            is HospitalIntent.SomeIntentWithParams -> {
                //do sth(intent.params)
            }
        }
    }

    init {
        launch {
            val location = async { getCurrentLocation() }.await()
            if (location != null) {
                getDetailHospitalInfo(location.latitude, location.longitude)
            } else {
                _eventFlow.emit(
                    HospitalEvent.DataFetch.Error(
                        userMessage = "위치 정보를 가져올 수 없어 병원 정보를 조회할 수 없습니다.",
                        exceptionMessage = "Failed to get location in init"
                    )
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun getCurrentLocation(): android.location.Location? {
        return try {
            fusedLocationClient.lastLocation.await()
        } catch (e: Exception) {
            _eventFlow.emit(
                HospitalEvent.DataFetch.Error(
                    userMessage = "위치 정보 조회에 실패했습니다.",
                    exceptionMessage = e.message
                )
            )
            null
        }
    }
    private suspend fun getDetailHospitalInfo(lat: Double, lng: Double) {
        _state.value = HospitalState.OnProgress

        runCatching {
            getDetailHospitalInfoUseCase(
                hospitalId = hospitalId,
                lat = lat,
                lng = lng
            )
        }.onSuccess { result ->
            _hospital.value = result.getOrThrow()
        }.onFailure { exception ->
            _eventFlow.emit(
                HospitalEvent.DataFetch.Error(
                    userMessage = "병원 상세 정보 조회에 실패했습니다.",
                    exceptionMessage = exception.message
                )
            )
        }
        _state.value = HospitalState.Init
    }
}