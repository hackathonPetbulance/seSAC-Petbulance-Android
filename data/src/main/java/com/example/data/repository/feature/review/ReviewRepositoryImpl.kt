package com.example.data.repository.feature.review

import com.example.data.remote.network.feature.reviews.ReviewApi
import com.example.data.remote.network.feature.reviews.model.ReviewListDto
import com.example.data.utils.safeApiCall
import com.example.domain.model.feature.review.ReviewList
import com.example.domain.repository.feature.review.ReviewRepository
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val reviewApi: ReviewApi
) : ReviewRepository {

    override suspend fun getReviews(
        region: String?,
        animalType: String?,
        receipt: Boolean?,
        size: Int?,
        cursorId: Long?
    ): Result<ReviewList> {
        return safeApiCall<ReviewListDto> {
            reviewApi.getReviews(
                region = region,
                animalType = animalType,
                receipt = receipt,
                size = size,
                cursorId = cursorId
            )
        }.map { it.toDomain() }
    }
}