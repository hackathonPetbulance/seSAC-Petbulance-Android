package com.example.domain.model.feature.review

import com.example.domain.model.type.AnimalCategory
import java.time.LocalDateTime

/* TODO : 실제 사용되는 명세로 수정 필요 */
data class HospitalReview(
    val reviewId: Long,
    val title: String,
    val content: String,
    val hospitalName: String,
    val animalCategory: AnimalCategory,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun stub() = HospitalReview(
            reviewId = 1L,
            title = "title",
            content = "갑자기 햄스터가 쓰러져서 패닉이었는데, 펫뷸런스에서 병원 찾아서 바로 맡겼더니 지금 안정됐어요. 너무너무 다행이에요.",
            hospitalName = "마포특수동물병원",
            animalCategory = AnimalCategory.SMALL_MAMMAL,
            createdAt = LocalDateTime.now(),
        )
    }
}