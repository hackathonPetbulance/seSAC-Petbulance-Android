package com.example.presentation.screen.search

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.SavedStateHandle
import com.example.domain.model.feature.hospitals.HospitalDetail
import com.example.domain.usecase.feature.hospital.GetDetailHospitalInfoUseCase
import com.example.presentation.screen.hospital.HospitalEvent
import com.example.presentation.screen.hospital.HospitalState
import com.example.presentation.utils.BaseViewModel
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getDetailHospitalInfoUseCase: GetDetailHospitalInfoUseCase,
    application: Application
) : BaseViewModel() {

    private val defaultLocation = Location("default").apply {
        latitude = 37.5665
        longitude = 126.9780
    }

    private val hospitalId: Long = savedStateHandle["hospitalId"] ?: 0L

    private val _currentUserLocation = MutableStateFlow(defaultLocation)
    val currentUserLocation: StateFlow<Location> = _currentUserLocation

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)
    private val _state = MutableStateFlow<SearchState>(SearchState.Init)
    val state: StateFlow<SearchState> = _state

    private val _eventFlow = MutableSharedFlow<SearchEvent>(replay = 1)
    val eventFlow: SharedFlow<SearchEvent> = _eventFlow

    private val _hospital = MutableStateFlow<HospitalDetail?>(null)
    val hospital: StateFlow<HospitalDetail?> = _hospital

    fun onIntent(intent: SearchIntent) {}

    init {
        launch {
            checkAndFetchLocation(application.applicationContext)
        }
    }

    @SuppressLint("MissingPermission")
    private fun checkAndFetchLocation(context: Context) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("siria22", "Search VM - Has location permission")
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                launch {
                    val currentLocation = location ?: defaultLocation
                    _currentUserLocation.value = currentLocation
                    getDetailHospitalInfo(currentLocation.latitude, currentLocation.longitude)
                }
            }.addOnFailureListener {
                launch {
                    _eventFlow.emit(
                        SearchEvent.DataFetch.Error(
                            userMessage = "위치 정보를 가져오는 데 실패했습니다.",
                            exceptionMessage = it.message
                        )
                    )
                }
            }
        } else {
            launch {
                _eventFlow.emit(
                    SearchEvent.PermissionCheck.NoPermissionGranted(
                        exceptionMessage = "Location permission not granted."
                    )
                )
            }
        }
    }

    private suspend fun getDetailHospitalInfo(userLat: Double, userLng: Double) {
        _state.value = SearchState.OnProgress

        runCatching {
            getDetailHospitalInfoUseCase(
                hospitalId = hospitalId,
                userLat = userLat,
                userLng = userLng
            )
        }.onSuccess { result ->
            Log.d("siria22", "getDetailHospitalInfo($userLat, $userLng) success:\n" +
                    "result : $result")
            _hospital.value = result
        }.onFailure { exception ->
            _eventFlow.emit(
                SearchEvent.DataFetch.Error(
                    userMessage = "병원 상세 정보 조회에 실패했습니다.",
                    exceptionMessage = exception.message
                )
            )
        }
        _state.value = SearchState.Init
    }
}
