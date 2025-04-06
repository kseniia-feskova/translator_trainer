package com.domain.usecase.course

import translator.data.prefs.IDataStoreManager
import com.domain.mapper.toData
import com.domain.mapper.toUI
import com.presentation.model.CourseUI
import com.presentation.test.dummyCourses
import com.presentation.usecases.course.ICoursesOnPrefsUseCases

class CoursesOnPrefsUseCases(private val dataStorage: IDataStoreManager) :
    ICoursesOnPrefsUseCases {

    override suspend fun getAll(): List<CourseUI> {
        return dataStorage.getCourses().map { it.toUI() }.ifEmpty { dummyCourses }
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