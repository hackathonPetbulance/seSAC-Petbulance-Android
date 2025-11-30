package com.example.domain.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * 현재 요일을 영문 약어 대문자 (e.g., "MON", "TUE")로 반환합니다.
 * @return "MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN" 중 하나
 */
fun getCurrentDayOfWeekAbbreviated(): String {
    val now = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("E", Locale.ENGLISH)
    return now.format(formatter).uppercase(Locale.ENGLISH)
}

fun String.toKorean() : String {
    return when(this) {
        "MON" -> "월요일"
        "TUE" -> "화요일"
        "WED" -> "수요일"
        "THU" -> "목요일"
        "FRI" -> "금요일"
        "SAT" -> "토요일"
        "SUN" -> "일요일"
        "HOLIDAY" -> "공휴일"
        else -> ""
    }
}