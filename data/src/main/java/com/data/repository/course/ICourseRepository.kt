package com.data.repository.course

import com.data.model.base.Result
import com.data.model.course.CourseEntity
import com.data.model.course.add.AddCourseRequest
import com.data.model.course.get.GetAllCoursesRequest
import java.util.UUID

interface ICourseRepository {

    suspend fun addCourse(request:AddCourseRequest): Result<CourseEntity>

    suspend fun getCourseById(id: UUID): Result<CourseEntity>

    suspend fun getAllCoursesForUser(request: GetAllCoursesRequest): Result<List<CourseEntity>>

}