package data.model.user

import data.model.course.CourseEntity

data class UserEntity(
    val id: String,
    val email: String,
    val username: String? = null,
    val photoUrl: String? = null,
    val courses: List<CourseEntity> = emptyList()
)
