package domain.utils

import data.model.course.CourseEntity
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

@Suppress("unused")
object JsonBridge {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    fun encodeCourse(course: CourseEntity): String {
        return json.encodeToString(CourseEntity.serializer(), course)
    }

    fun decodeCourse(jsonString: String): CourseEntity {
        return json.decodeFromString(CourseEntity.serializer(), jsonString)
    }

    fun encodeCourseList(courses: List<CourseEntity>): String {
        return json.encodeToString(ListSerializer(CourseEntity.serializer()), courses)
    }

    fun decodeCourseList(jsonString: String): List<CourseEntity> {
        return json.decodeFromString(ListSerializer(CourseEntity.serializer()), jsonString)
    }
}