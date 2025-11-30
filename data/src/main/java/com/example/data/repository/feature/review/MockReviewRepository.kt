package com.example.data.repository.feature.review

import com.example.domain.model.feature.review.ReviewList
import com.example.domain.repository.feature.review.ReviewRepository
import javax.inject.Inject

class MockReviewRepository @Inject constructor() : ReviewRepository {
    override suspend fun getReviews(
        region: String?,
        animalType: String?,
        receipt: Boolean?,
        size: Int?,
        cursorId: Long?
    ): Result<ReviewList> {
        return Result.success(ReviewList.stub())
    }

}