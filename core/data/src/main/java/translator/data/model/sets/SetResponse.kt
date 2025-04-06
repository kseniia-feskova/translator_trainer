package translator.data.model.sets

import translator.data.model.WordEntity
import java.util.UUID

data class SetResponse(
    val id: UUID,
    val name: String,
    val isDefault: Boolean = false,
    val words: List<WordEntity> = emptyList()
)
