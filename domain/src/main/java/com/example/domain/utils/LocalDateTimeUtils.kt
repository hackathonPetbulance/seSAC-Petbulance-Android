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