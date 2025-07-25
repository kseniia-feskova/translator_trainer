package mapper

import data.model.sets.SetResponse
import presentation.model.SetOfCards

fun List<SetResponse>.toUI(): List<SetOfCards> {
    return this.map {
        it.toUI()
    }
}

fun SetResponse.toUI(): SetOfCards {
    return SetOfCards(
        id = id,
        title = name,
        isDefault = isDefault,
        words = words.map { it.toWord() },
    )
}

fun SetResponse.toUIWithoutWords(): SetOfCards {
    return SetOfCards(
        id = id,
        title = name,
        isDefault = isDefault,
        words = emptyList()
    )
}