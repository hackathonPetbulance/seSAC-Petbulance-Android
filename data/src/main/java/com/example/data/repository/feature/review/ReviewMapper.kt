package com.example.data.repository.feature.review

import com.example.data.remote.network.feature.reviews.model.ReviewDto
import com.example.data.remote.network.feature.reviews.model.ReviewListDto
import com.example.domain.model.feature.review.Review
import com.example.domain.model.feature.review.ReviewList
import java.time.LocalDateTime

fun ReviewListDto.toDomain(): ReviewList {
    return ReviewList(
        list = this.list.map { it.toDomain() },
        nextCursorId = this.nextCursorId,
        hasNext = this.hasNext
    )
}

fun ReviewDto.toDomain(): Review {
    return Review(
        receiptCheck = this.receiptCheck,
        id = this.id,
        hospitalImage = this.hospitalImage,
        hospitalId = this.hospitalId,
        hospitalName = this.hospitalName,
        treatmentService = this.treatmentService,
        detailAnimalType = this.detailAnimalType,
        reviewContent = this.reviewContent,
        totalRating = this.totalRating,
        author = animalCommunityNicknames.random(),         /* TODO : Mocking됨 */
        likeCount = (0..30).random(),                 /* TODO : Mocking됨 */
        createdAt = deterministicDateFromId(this.id)   /* TODO : Mocking됨 */
    )
}

val animalCommunityNicknames = listOf(
    "꼬리살랑꾼",
    "솜방맹이",
    "간식셔틀러",
    "멍냥합사",
    "우다다캣",
    "댕댕쓰",
    "냥집사일기",
    "발바닥젤리",
    "털뿜뿜",
    "어흥이는",
    "왈가닥견",
    "찹쌀떡냥",
    "행복하멍",
    "꾹꾹이장인",
    "물고기자리",
    "꼬물꼬물",
    "냥냥월드",
    "강아지밥",
    "폴짝토끼",
    "행복전도사"
)

private fun deterministicDateFromId(id: Long): LocalDateTime {
    val baseDateTime = LocalDateTime.of(2025, 11, 30, 22, 50, 0)

    val daysToSubtract = id % 365
    val hoursToSubtract = (id / 365) % 24
    val minutesToSubtract = (id / (365 * 24)) % 60

    return baseDateTime
        .minusDays(daysToSubtract)
        .minusHours(hoursToSubtract)
        .minusMinutes(minutesToSubtract)
}