package com.example.domain.usecase.feature.review

import com.example.domain.model.feature.review.ReviewList
import com.example.domain.repository.feature.review.ReviewRepository
import javax.inject.Inject

class GetReviewsUseCase @Inject constructor(
    private val repository: ReviewRepository
) {
    suspend operator fun invoke(
        region: String? = null,
        animalType: String? = null,
        receipt: Boolean? = null,
        size: Int? = 10,
        cursorId: Long? = null
    ): Result<ReviewList> {
        return repository.getReviews(region, animalType, receipt, size, cursorId)
    }
}