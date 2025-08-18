package usecase.course

import data.mock.dummyCourses
import data.prefs.IDataStoreManager
import mapper.toData
import mapper.toUI
import presentation.model.CourseUI
import presentation.usecases.course.ICoursesOnPrefsUseCases

class CoursesOnPrefsUseCases(private val dataStorage: IDataStoreManager) : ICoursesOnPrefsUseCases {

    override suspend fun getAll(): List<CourseUI> {
        return dataStorage.getCourses().ifEmpty { dummyCourses }.map { it.toUI() }
    }

    override fun saveAll(courses: List<CourseUI>) {
        dataStorage.saveCourses(courses.map { it.toData() })
    }

    override suspend fun saveOne(course: CourseUI) {
        dataStorage.saveCourse(course = course.toData())
    }

    override suspend fun resetCourse() {
        dataStorage.saveCourse(null)
    }

    override suspend fun getCourse(): CourseUI? {
        return dataStorage.getCourse()?.toUI()
    }

}