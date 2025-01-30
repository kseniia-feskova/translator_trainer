package com.data.model

import com.data.model.course.CourseEntity
import java.util.UUID

data class UserEntity(
    val id: UUID,
    val email: String,
    val username: String? = null,
    val photoUrl: String? = null,
    val courses: List<CourseEntity> = emptyList()
)
