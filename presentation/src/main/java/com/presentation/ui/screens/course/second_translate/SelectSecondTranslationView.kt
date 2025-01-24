package com.presentation.ui.screens.course.second_translate

//
//@Composable
//fun SelectSecondTranslationView(
//    modifier: Modifier = Modifier,
//    state: CourseData.SelectSecondTranslationData,
//    checkSelected: (String) -> Unit = {},
//) {
//    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
//    val cardHeight = screenHeight * 0.25f
//    Column(Modifier.then(modifier), verticalArrangement = Arrangement.spacedBy(48.dp)) {
//        com.presentation.utils.CardView(
//            modifier = Modifier
//                .align(Alignment.CenterHorizontally)
//                .padding(horizontal = 16.dp),
//            cardHeight = cardHeight,
//            firstWordUIPreview = state.currentWord.resText,
//            secondWordUIPreview = state.nextWord?.resText,
//        )
//
//        ChipSelector(
//            modifier = Modifier
//                .padding(horizontal = 8.dp)
//                .padding(bottom = 54.dp),
//            options = state.translations,
//            selectedOption = state.selectedOption,
//            onOptionSelected = { checkSelected(it) },
//            correctAnswer = state.currentWord.originalText
//        )
//    }
//}
//
//@Preview
//@Composable
//fun SelectSecondTranslationPreview(
//    modifier: Modifier = Modifier,
//    stateIndex: Int = 1,
//    onStateChange: () -> Unit = {}
//) {
//    val coroutineScope = rememberCoroutineScope()
//    var state by remember {
//        mutableStateOf(
//            CourseData.SelectSecondTranslationData(
//                currentWord = listOfDummyCards.first(),
//                nextWord = listOfDummyCards[stateIndex + 1],
//                translations = listOfDummyCards.map { it.resText },
//                currentWordIndex = 1,
//                allWordsCount = listOfDummyCards.size
//            )
//        )
//    }
//
//    Box(
//        modifier = Modifier
//            .background(primaryColor)
//            .then(modifier)
//    ) {
//        SelectSecondTranslationView(
//            state = state
//        ) { selected ->
//            state = state.copy(selectedOption = selected)
//            coroutineScope.launch {
//                delay(1300L)
//                val word = state.currentWord
//                if (selected == word.resText) {
//                    state = state.copy(
//                        currentWord = listOfDummyCards[stateIndex + 1],
//                        nextWord = listOfDummyCards[stateIndex + 2],
//                        currentWordIndex = 2,
//                        selectedOption = ""
//                    )
//                    onStateChange()
//                }
//            }
//        }
//    }
//}
//
//@Composable
//@Preview
//fun CourseFrameViewPreview() {
//    var wordIndex by remember {
//        mutableIntStateOf(1)
//    }
//    Box(modifier = Modifier.background(primaryColor)) {
//        CourseFrameView(
//            CourseData.SelectTranslationData(
//                WordUI(UUID.fromString("0"), "Text", "Текст", Level.KNOW),
//                null,
//                emptyList(),
//                selectedOption = "",
//                currentWordIndex = wordIndex
//            )
//        ) {
//            SelectSecondTranslationPreview(modifier = Modifier.align(Alignment.Center), wordIndex) {
//                wordIndex += 1
//            }
//        }
//    }
//}

/*
* -> Шапочка общая для разных уровней. На ней название набора, кнопка "закрыть" и прогресс по текущему уроку
*
* Между ними контент смециальный для каждого типа уровня
*
*
* Набор карточек, но их нельзя переворачивать или свайпать
* Варианты перевода.
* После клика проверяем совпадение. Если совпало - то зеленная подсветка и смена слова
* если не совпало - то красная подсветка и пробуем снова
*
* -> Футтер с текстом "Не знаю". Можно кликнуть и пропустить слово
*
* */