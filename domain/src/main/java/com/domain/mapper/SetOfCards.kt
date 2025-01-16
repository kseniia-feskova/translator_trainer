package com.domain.mapper

import com.data.model.sets.SetResponse
import com.presentation.model.SetOfCards

//fun SetOfCards.toData(): SetOfWords {
//    return SetOfWords(
//        name = title,
//        level = com.data.model.SetLevel.EASY,//level.toData(),
//        userId = courseId,
//
//    )
//}
//
//fun SetLevel.toData(): com.data.model.SetLevel {
//    return when (this) {
//        SetLevel.EASY -> com.data.model.SetLevel.EASY
//        SetLevel.MEDMIUM -> com.data.model.SetLevel.MEDIUM
//        SetLevel.HARD -> com.data.model.SetLevel.HARD
//    }
//}


//fun SetOfWords.toPresentation(): SetOfCards {
//    return SetOfCards(
//        id = id,
//        title = name,
//      //  level = level.toPresentation(),
//        courseId = userId,
//        isDefault =
//    )
//}

//fun SetWithWords.toPresentation(): SetOfCards {
//    return SetOfCards(
//        id = set.id,
//        title = set.name,
//        level = set.level.toPresentation(),
//        courseId = set.userId,
//        setOfWords = words.map { it.toWord() }.toSet()
//    )
//}

//fun com.data.model.SetLevel.toPresentation(): SetLevel {
//    return when (this) {
//        com.data.model.SetLevel.EASY -> SetLevel.EASY
//        com.data.model.SetLevel.MEDIUM -> SetLevel.MEDMIUM
//        com.data.model.SetLevel.HARD -> SetLevel.HARD
//    }
//}

fun List<SetResponse>.toUI(): List<SetOfCards> {
    return this.map {
        it.toUI()
    }
}

fun SetResponse.toUI(): SetOfCards {
    return SetOfCards(
        id = id,
        title = name,
        isDefault = is_default,
        setOfWords = emptySet(),
        courseId = course.id
    )
}