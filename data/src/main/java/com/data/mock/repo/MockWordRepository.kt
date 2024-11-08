package com.data.mock.repo

import com.data.mock.model.SetOfCards
import com.data.mock.model.Status
import com.data.mock.model.Word
import com.data.model.SetLevel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MockWordRepository() : IMockWordRepository {

    private val _setsFlow = MutableStateFlow(listOfSets.toList())
    override val setsFlow: StateFlow<List<SetOfCards>> = _setsFlow

    override fun addNewWord(setId: Int, newWord: Word) {
        val set = getSetOfWords(setId)
        val words = set.setOfWords.toMutableSet()
        words.add(newWord)
        val newSet = set.copy(
            setOfWords = words
        )
        updateSetOfWords(newSet)
    }

    override fun getSetOfWords(setId: Int): SetOfCards {
        val set = listOfSets.find { it.id == setId }
        if (set == null) {
            val newSet = SetOfCards(setId, "Набор $setId", SetLevel.EASY, emptySet())
            listOfSets.add(newSet)
            return newSet
        } else {
            return set
        }
    }

    private fun updateSetOfWords(setOfCards: SetOfCards) {
        val setIndex = listOfSets.indexOfFirst { it.id == setOfCards.id }
        if (setIndex != -1) {
            listOfSets[setIndex] = setOfCards
            updateFlow()
        }
    }

    private fun updateFlow() {
        _setsFlow.value = listOfSets.toList()
    }

    companion object {
        private val listOfSets = mutableListOf<SetOfCards>()
        val listOfSetsMock = mutableListOf(
            SetOfCards(
                id = 1,
                title = "Первый набор",
                level = SetLevel.EASY,
                setOfWords = setOf(
                    Word("1", "Katze", "Кошка", Status.NEW),
                    Word("2", "Mutter", "Мать", Status.NEW),
                    Word("3", "Vater", "Отец", Status.LEARNING),
                    Word("4", "Leben", "Жизнь", Status.GOOD_LEARNING),
                    Word("5", "Liebe", "Любовь", Status.KNOW),
                    Word("6", "Mutter", "Мать", Status.NEW),
                    Word("7", "Vater", "Отец", Status.LEARNING),
                    Word("8", "Leben", "Жизнь", Status.GOOD_LEARNING),
                    Word("9", "Liebe", "Любовь", Status.KNOW),
                ).sortedBy { it.status }.toSet()
            ),
            SetOfCards(
                id = 2,
                title = "Второй набор",
                level = SetLevel.EASY,
                emptySet()
            ),
            SetOfCards(
                id = 3,
                title = "Третий набор",
                level = SetLevel.EASY,
                emptySet()
            )
        )
    }
}