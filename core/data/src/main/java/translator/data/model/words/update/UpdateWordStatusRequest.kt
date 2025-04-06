package translator.data.model.words.update

import translator.data.model.WordStatus

data class UpdateWordStatusRequest(
    val status: WordStatus
)
