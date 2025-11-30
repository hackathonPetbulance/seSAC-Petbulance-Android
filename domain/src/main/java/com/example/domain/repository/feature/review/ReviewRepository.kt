package com.example.domain.repository.feature.review

import com.example.domain.model.feature.review.ReviewList

interface ReviewRepository {
    suspend fun getReviews(
        region: String?,
        animalType: String?,
        receipt: Boolean?,
        size: Int?,
        cursorId: Long?
    ): Result<ReviewList>
}