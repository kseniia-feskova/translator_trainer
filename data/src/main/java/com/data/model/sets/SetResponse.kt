package com.data.model.sets

import com.data.model.course.CourseEntity
import java.util.UUID

data class SetResponse(
    val id: UUID,
    val name: String,
    val is_default: Boolean = false,
    val course: CourseEntity
)