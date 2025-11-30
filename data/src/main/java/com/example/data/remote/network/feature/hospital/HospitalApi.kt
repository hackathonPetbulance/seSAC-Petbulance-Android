package com.example.data.remote.network.feature.hospital

import com.example.data.common.di.network.BASE_URL
import com.example.domain.model.type.HospitalFilterType
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class HospitalApi @Inject constructor(
    private val client: HttpClient
) {
    private val baseUrl = "${BASE_URL}/hospitals/"

    suspend fun getMatchingHospitals(
        filter: HospitalFilterType,
        species: String,
        lat: Double,
        lng: Double
    ): HttpResponse {
        return client.get(baseUrl+"matching") {
            contentType(ContentType.Application.Json)
            parameter("filter", filter.name)
            species.forEach { parameter("species", it) }
            parameter("lat", lat)
            parameter("lng", lng)
        }
    }

    suspend fun getHospitalDetailInfo(
        hospitalId: Long,
        lat: Double,
        lng: Double
    ): HttpResponse {
        return client.get("$baseUrl$hospitalId/matching") {
            contentType(ContentType.Application.Json)
            parameter("lat", lat)
            parameter("lng", lng)
        }
    }
}