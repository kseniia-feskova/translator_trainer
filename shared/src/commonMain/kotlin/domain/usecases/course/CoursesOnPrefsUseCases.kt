package domain.usecases.course

import data.mock.dummyCourses
import data.model.course.CourseEntity
import data.prefs.IDataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CoursesOnPrefsUseCases(private val dataStorage: IDataStoreManager) : ICoursesOnPrefsUseCases {

    override suspend fun getAll(): List<CourseEntity> {
        return dataStorage.getCourses().ifEmpty { dummyCourses }
    }

    override fun saveAll(courses: List<CourseEntity>) {
        dataStorage.saveCourses(courses)
    }

    override suspend fun saveOne(course: CourseEntity) {
        dataStorage.saveCourse(course = course)
    }

    override suspend fun resetCourse() {
        dataStorage.saveCourse(null)
    }

    override suspend fun getCourse(): CourseEntity? {
        return dataStorage.getCourse()
    }

    override fun getCourseFlow(): Flow<CourseEntity?> = flow {
        emit(dataStorage.getCourse())
    }

}