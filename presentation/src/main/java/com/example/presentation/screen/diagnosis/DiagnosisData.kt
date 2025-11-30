package com.example.presentation.screen.diagnosis

import android.net.Uri
import androidx.core.net.toUri

data class DiagnosisData(
    val imageUris: List<Uri?>,
    val animalSpecies: String,
    val description: String
) {
    companion object {
        fun empty() = DiagnosisData(
            imageUris = listOf( "null".toUri(), null, null),
            animalSpecies = "",
            description = ""
        )
    }
}