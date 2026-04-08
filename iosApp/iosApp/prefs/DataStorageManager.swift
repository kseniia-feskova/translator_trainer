//
//  DataStorageManager.swift
//  iosApp
//
//  Created by  Kseniia Feskova on 07.04.2026.
//

import Foundation
import Shared

import Foundation

class DataStoreManagerIOS: IDataStoreManager {
    
    private let defaults = UserDefaults.standard

    private let userIdKey = "user_id"
    private let courseKey = "course"
    private let emailKey = "email"
    private let isGuestKey = "is_guest"
    private let isOfflineKey = "is_offline"
    private let coursesKey = "courses_list"

    // MARK: - USER ID

    func saveUserId(id: String?) async throws {
        if let id = id, !id.isEmpty {
            defaults.set(id, forKey: userIdKey)
        } else {
            defaults.removeObject(forKey: userIdKey)
        }
    }

    func getUserId() async throws -> String? {
        let value = defaults.string(forKey: userIdKey)
        return value?.isEmpty == true ? nil : value
    }

    // MARK: - EMAIL

    func saveEmail(email: String) async throws {
        defaults.set(email, forKey: emailKey)
    }

    func getEmail() async throws -> String? {
        return defaults.string(forKey: emailKey)
    }

    // MARK: - GUEST MODE

    func setGuestMode() async throws {
        defaults.set(true, forKey: isGuestKey)
    }

    func isGuest(completionHandler: @escaping (KotlinBoolean?, Error?) -> Void) {
        let value = defaults.bool(forKey: isGuestKey)
        completionHandler(KotlinBoolean(value: value), nil)
    }

    func resetGuestMode() async throws {
        defaults.set(false, forKey: isGuestKey)
    }

    // MARK: - OFFLINE MODE

    func isOfflineMode(completionHandler: @escaping (KotlinBoolean?, (any Error)?) -> Void) {
        let value = defaults.bool(forKey: isOfflineKey)
        completionHandler(KotlinBoolean(value: value), nil)
    }

    func setOfflineMode(set: Bool) async throws {
        defaults.set(set, forKey: isOfflineKey)
    }

    // MARK: - COURSE (один объект)

    func saveCourse(course: CourseEntity?) async throws {
        if let course = course {
            let json = JsonBridge().encodeCourse(course: course)
            defaults.set(json, forKey: courseKey)
        } else {
            defaults.removeObject(forKey: courseKey)
        }
    }

    func getCourse() async throws -> CourseEntity? {
        guard let data = defaults.string(forKey: courseKey) else { return nil }
        return JsonBridge().decodeCourse(jsonString: data)
    }

    // MARK: - COURSES LIST

    func saveCourses(courses: [CourseEntity]) {
        let data = JsonBridge().encodeCourseList(courses: courses)
        defaults.set(data, forKey: coursesKey)
    }

    func getCourses() -> [CourseEntity] {
        guard let data = defaults.string(forKey: coursesKey) else { return [] }
        return JsonBridge().decodeCourseList(jsonString: data)
    }
}
