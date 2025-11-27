package com.example.presentation.utils.error

import android.util.Log

data class ErrorDialogState(
    val userMessage: String,
    val exceptionMessage: String?,
    val isErrorDialogVisible: Boolean
) {
    fun logErrorEvent() {
        Log.e(
            "siria22",
            "userMessage : $userMessage\nexceptionMessage : ${exceptionMessage ?: "Unknown error"}"
        )
    }

    fun toggleVisibility(): ErrorDialogState = copy(isErrorDialogVisible = !isErrorDialogVisible)

    companion object {
        fun idle() =
            ErrorDialogState(userMessage = "", exceptionMessage = "", isErrorDialogVisible = false)

        fun fromErrorEvent(errorEvent: ErrorEvent): ErrorDialogState =
            ErrorDialogState(
                userMessage = errorEvent.userMessage,
                exceptionMessage = errorEvent.exceptionMessage,
                isErrorDialogVisible = true
            )
    }
}