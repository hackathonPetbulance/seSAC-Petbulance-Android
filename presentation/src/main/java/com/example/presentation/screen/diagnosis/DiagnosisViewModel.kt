package com.example.presentation.screen.diagnosis

import android.annotation.SuppressLint
import android.app.Application
import android.net.Uri
import android.util.Log
import com.example.domain.model.feature.diagnosis.AiDiagnosis
import com.example.domain.model.feature.hospitals.MatchedHospital
import com.example.domain.model.type.HospitalFilterType
import com.example.domain.usecase.feature.diagnosis.RequestDiagnosisUseCase
import com.example.domain.usecase.feature.hospital.GetHospitalWithFilterUseCase
import com.example.presentation.utils.BaseViewModel
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * Is shared by DiagnosisScreen and Report Screen
 */
@HiltViewModel
class DiagnosisViewModel @Inject constructor(
    private val requestDiagnosisUseCase: RequestDiagnosisUseCase,
    private val getHospitalWithFilterUseCase: GetHospitalWithFilterUseCase,
    application: Application
) : BaseViewModel() {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    private val _state = MutableStateFlow<DiagnosisState>(DiagnosisState.Init)
    val state: StateFlow<DiagnosisState> = _state

    private val _eventFlow = MutableSharedFlow<DiagnosisEvent>(replay = 1)
    val eventFlow: SharedFlow<DiagnosisEvent> = _eventFlow

    private val _aiDiagnosis = MutableStateFlow<AiDiagnosis?>(null)
    val aiDiagnosis: StateFlow<AiDiagnosis?> = _aiDiagnosis

    private val _imageUris = MutableStateFlow<List<Uri?>>(listOf(null, null, null))
    val imageUris: StateFlow<List<Uri?>> = _imageUris

    private val _animalSpecies = MutableStateFlow("")
    val animalSpecies: StateFlow<String> = _animalSpecies

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    private val _userLocation = MutableStateFlow("")
    val userLocation: StateFlow<String> = _userLocation

    private val _matchedHospitals = MutableStateFlow<List<MatchedHospital>>(emptyList())
    val matchedHospitals: StateFlow<List<MatchedHospital>> = _matchedHospitals


    fun onIntent(intent: DiagnosisIntent) {
        when (intent) {
            is DiagnosisIntent.UpdateAnimalSpecies -> {
                _animalSpecies.value = intent.species
            }

            is DiagnosisIntent.UpdateDescription -> {
                _description.value = intent.description
            }

            is DiagnosisIntent.UpdateImageUris -> {
                val updatedList = _imageUris.value.toMutableList()
                updatedList[intent.index] = intent.uri
                _imageUris.value = updatedList
            }

            is DiagnosisIntent.RequestDiagnosis -> {
                launch {
                    requestDiagnosis(onUpload = intent.onUpload)
                }
            }

            is DiagnosisIntent.MatchHospitalByFilter -> {
                launch { getNearByHospital(intent.filter) }
            }
        }
    }

    private suspend fun requestDiagnosis(onUpload: (Long, Long) -> Unit) {
        _state.value = DiagnosisState.OnProgress

        runCatching {
            requestDiagnosisUseCase(
                images = _imageUris.value.take(3).map { it.toString() },
                animalType = _animalSpecies.value,
                symptom = _description.value,
                onUpload = onUpload
            )
        }.onSuccess { result ->
            Log.d(
                "siria22", "Request Sent\n" +
                        "${_imageUris.value.map { it.toString() }}, \n" +
                        "${_animalSpecies.value}, \n" +
                        _description.value
            )
            result.getOrNull()?.let {
                _aiDiagnosis.value = it
                _eventFlow.emit(DiagnosisEvent.RequestSuccess)
            }
        }.onFailure { exception ->
            _eventFlow.emit(
                DiagnosisEvent.DataFetch.Error(
                    userMessage = "진단 요청 중 오류가 발생했습니다.",
                    exceptionMessage = exception.message
                )
            )
        }
        _state.value = DiagnosisState.Init
    }

    @SuppressLint("MissingPermission")
    private suspend fun getNearByHospital(filter: HospitalFilterType) {
        _state.value = DiagnosisState.OnProgress

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                launch {
                    runCatching {
                        getHospitalWithFilterUseCase(
                            filter = filter,
                            species = _animalSpecies.value,
                            location.latitude,
                            location.longitude
                        )
                    }.onSuccess { result ->
                        Log.d(
                            "siria22",
                            "Request Success from : ${location.latitude},${location.longitude}"
                        )
                        _matchedHospitals.value = result.getOrThrow()
                    }.onFailure { ex ->
                        _eventFlow.emit(
                            DiagnosisEvent.DataFetch.Error(
                                userMessage = "근처 병원 조회에 실패했어요.",
                                exceptionMessage = ex.message
                            )
                        )
                    }
                }
            } else {
                launch {
                    _eventFlow.emit(
                        DiagnosisEvent.DataFetch.Error(
                            userMessage = "위치 정보를 가져올 수 없습니다.",
                            exceptionMessage = "Location is null"
                        )
                    )
                }
            }
        }.addOnFailureListener { ex ->
            launch {
                _eventFlow.emit(
                    DiagnosisEvent.DataFetch.Error(
                        userMessage = "위치 정보 조회에 실패했습니다.",
                        exceptionMessage = ex.message
                    )
                )
            }
        }
        _state.value = DiagnosisState.Init
    }
}