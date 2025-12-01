package com.example.domain.model.type

// Use First level category only in this project
enum class AnimalCategory {
    SMALL_MAMMAL, // 소형 포유류
    BIRD,         // 조류
    REPTILE,      // 파충류
    AMPHIBIAN,    // 양서류
    FISH          // 어류
}

enum class AnimalSpecies(val koreanName: String, val category: AnimalCategory) {
    // 소형 포유류 (Small Mammal)
    HAMSTER("햄스터", AnimalCategory.SMALL_MAMMAL),
    GUINEAPIG("기니피그", AnimalCategory.SMALL_MAMMAL),
    CHINCHILLA("친칠라", AnimalCategory.SMALL_MAMMAL),
    RABBIT("토끼", AnimalCategory.SMALL_MAMMAL),
    HEDGEHOG("고슴도치", AnimalCategory.SMALL_MAMMAL),
    FERRET("페럿", AnimalCategory.SMALL_MAMMAL),
    SUGAR_GLIDER("슈가글라이더", AnimalCategory.SMALL_MAMMAL),
    PRAIRIE_DOG("프레리도그", AnimalCategory.SMALL_MAMMAL),
    FLYING_SQUIRREL("하늘다람쥐", AnimalCategory.SMALL_MAMMAL),
    OTHER_SMALL_MAMMALS("기타소동물", AnimalCategory.SMALL_MAMMAL),

    // 조류 (Avian)
    PARROT("앵무새", AnimalCategory.BIRD),
    FINCH_TYPES("핀치류", AnimalCategory.BIRD),
    OTHER_BIRDS("기타조류", AnimalCategory.BIRD),

    // 파충류 (Reptile)
    GECKO("게코", AnimalCategory.REPTILE),
    OTHER_LIZARDS("기타 도마뱀", AnimalCategory.REPTILE),
    SNAKE("뱀", AnimalCategory.REPTILE),
    TURTLE("거북이", AnimalCategory.REPTILE),
    OTHER_REPTILES("기타 파충류", AnimalCategory.REPTILE),

    // 양서류 (Amphibian)
    FROG("개구리", AnimalCategory.AMPHIBIAN),
    AXOLOTL("우파루파", AnimalCategory.AMPHIBIAN),
    SALAMANDER("도롱뇽", AnimalCategory.AMPHIBIAN),
    OTHER_AMPHIBIANS("기타 양서류", AnimalCategory.AMPHIBIAN),

    // 어류 (Fish)
    ORNAMENTAL_FISH("관상어", AnimalCategory.FISH)
}

fun AnimalCategory.toKorean(): String {
    return when (this) {
        AnimalCategory.SMALL_MAMMAL -> "소형 포유류"
        AnimalCategory.BIRD -> "조류"
        AnimalCategory.REPTILE -> "파충류"
        AnimalCategory.AMPHIBIAN -> "양서류"
        AnimalCategory.FISH -> "어류"
    }
}

fun AnimalSpecies.toKorean(): String {
    return this.koreanName
}