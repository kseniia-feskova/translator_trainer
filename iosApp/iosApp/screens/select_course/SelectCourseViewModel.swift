//
//  SelectCourseViewModel.swift
//  iosApp
//
//  Created by  Kseniia Feskova on 02.04.2026.
//

import SwiftUI
import Combine

final class SelectCourseViewModel : ObservableObject {
    
    @Published var state: SelectCourseUIState = SelectCourseUIState()
    let event = PassthroughSubject<SelectCourseEvent, Never>()
    
}

/*
 private val getCourses: ICoursesOnPrefsUseCases,
    private val logout: ILogoutUseCase,
    private val addCourse: IAddCourseUseCase,
    private val translatorProvider: ITranslateModelProvider
 */


struct SelectCourseUIState {
    var selectedCourse: CourseUI? = nil
    var courses: [CourseUI] = []
}

enum SelectCourseEvent {
    case onCourseSelected
    case onContinueClicked
    case onBackClicked
}
