package usecase

import presentation.usecases.ITranslateWordUseCase
import domain.translate.ITranslateModelProvider
import mapper.toData
import presentation.utils.Language

class TranslateWordUseCase(
    private val manager: ITranslateModelProvider
) : ITranslateWordUseCase {

    override suspend fun invoke(
        text: String,
        originalLanguage: Language,
        resLanguage: Language,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        manager.translate(
            text,
            originalLanguage.toData(),
            resLanguage.toData(),
            onSuccess, onError
        )
    }

    override fun closeResources(){
        manager.close()
    }

}