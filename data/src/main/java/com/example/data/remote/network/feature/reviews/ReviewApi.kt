package com.example.data.remote.network.feature.reviews

import com.example.data.common.di.network.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class ReviewApi @Inject constructor(
    private val client: HttpClient
) {
    private val baseUrl = "${BASE_URL}/receipts/filter"

    suspend fun getReviews(
        region: String?,
        animalType: String?,
        receipt: Boolean?,
        size: Int?,
        cursorId: Long? = null
    ): HttpResponse {
        return client.get(baseUrl) {
            contentType(ContentType.Application.Json)
            region?.let { parameter("region", it) }
            animalType?.let { parameter("animalType", it) }
            receipt?.let { parameter("receipt", it) }
            size?.let { parameter("size", it) }
        }
    }
}