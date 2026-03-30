package domain.usecases

import data.translate.Language
import domain.translate.ITranslateModelProvider

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
            originalLanguage,
            resLanguage,
            onSuccess,
            onError
        )
    }

    override fun closeResources(){
        manager.close()
    }

}