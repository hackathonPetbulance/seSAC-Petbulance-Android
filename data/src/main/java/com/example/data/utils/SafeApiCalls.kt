package com.example.data.utils

import android.util.Log
import com.example.domain.exception.BadRequestException
import com.example.domain.exception.InternalServerErrorException
import com.example.domain.exception.InvalidCredentialsException
import com.example.domain.exception.NotFoundException
import com.example.domain.exception.UnknownNetworkException
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.Serializable

@Serializable
data class GlobalResponse<T>(
    val status: Int,
    val success: Boolean,
    val data: T? = null
)

@Serializable
data class ErrorResponse(
    val errorClassName: String,
    val message: String
)

suspend inline fun <reified T> safeApiCall(apiCall: suspend () -> HttpResponse): Result<T> {
    try {
        val response = apiCall()
        val responseString = response.body<String>()
        val json = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }

        if (response.status.value in 200..299) {
            val responseBody = json.decodeFromString<GlobalResponse<T>>(responseString)
            Log.d("siria22 - SafeApiCalls (Success)", responseString)

            if (responseBody.success) {
                responseBody.data?.let { return Result.success(it) }
                    ?: return Result.failure(Exception("Data is null"))
            } else {
                return Result.failure(
                    mapToDomainException(
                        responseBody.status,
                        "Request failed with status ${responseBody.status}"
                    )
                )
            }
        } else {
            // 실패 시 GlobalResponse<ErrorResponse>로 파싱
            Log.e("siria22 - SafeApiCalls (Error)", responseString)
            val errorBody = json.decodeFromString<GlobalResponse<ErrorResponse>>(responseString)
            return Result.failure(
                mapToDomainException(
                    errorBody.status,
                    errorBody.data?.message ?: "Unknown error"
                )
            )
        }
    } catch (e: Exception) {
        // 네트워크 오류 또는 예상치 못한 JSON 구조일 경우
        Log.e("siria22_FatalError", "Exception in safeApiCall", e)
        return Result.failure(e)
    }
}

fun mapToDomainException(code: Int, message: String): Exception {
    return when (code) {
        400 -> BadRequestException(message)
        401 -> InvalidCredentialsException(message)
        404 -> NotFoundException(message)
        500 -> InternalServerErrorException(message)
        else -> if (code in 400..499) {
            com.example.domain.exception.ClientException(code, message)
        } else if (code in 500..599) {
            com.example.domain.exception.ServerException(code, message)
        } else {
            UnknownNetworkException("Unknown error with code $code: $message")
        }
    }
}