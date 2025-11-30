package com.example.data.remote.network.feature.reviews.model

import kotlinx.serialization.Serializable

@Serializable
data class ReviewListDto(
    val list: List<ReviewDto>,
    val nextCursorId: Long?,
    val hasNext: Boolean
)

@Serializable
data class ReviewDto(
    val receiptCheck: Boolean,
    val id: Long,
    val hospitalImage: String?,
    val hospitalId: Long,
    val hospitalName: String,
    val treatmentService: String,
    val detailAnimalType: String,
    val reviewContent: String,
    val totalRating: Double
)