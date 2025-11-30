package com.example.domain.model.feature.review

import java.time.LocalDateTime

data class ReviewList(
    val list: List<Review>,
    val nextCursorId: Long?,
    val hasNext: Boolean
) {
    companion object {
        fun empty() = ReviewList(
            list = emptyList(),
            nextCursorId = null,
            hasNext = false
        )

        fun stub() = ReviewList(
            list = listOf(
                Review(
                    receiptCheck = true,
                    id = 1,
                    hospitalImage = "https://example.com/hospital.jpg",
                    hospitalId = 1,
                    hospitalName = "Example Hospital",
                    treatmentService = "Treatment Service",
                    detailAnimalType = "Detail Animal Type",
                    reviewContent = "Review Content",
                    totalRating = 4.5,
                    author = "Author",
                    likeCount = 24,
                    createdAt = LocalDateTime.now()
                )
            ),
            nextCursorId = 1,
            hasNext = true
        )
    }
}

data class Review(
    val receiptCheck: Boolean,
    val id: Long,
    val hospitalImage: String?,
    val hospitalId: Long,
    val hospitalName: String,
    val treatmentService: String,
    val detailAnimalType: String,
    val reviewContent: String,
    val totalRating: Double,
    val author: String,
    val likeCount: Int,
    val createdAt: LocalDateTime
)