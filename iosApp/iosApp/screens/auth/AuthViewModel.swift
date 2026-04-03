//
//  AuthViewModel.swift
//  iosApp
//
//  Created by  Kseniia Feskova on 08.10.2025.
//

import SwiftUI
import Combine

final class AuthViewModel: ObservableObject {
    
    /*
     private val login: ILoginUseCase,
         private val register: IRegisterUseCase,
         private val registerByFirebase: IRegisterWithFirebaseUseCase,
         private val getCourses: IGetAllCoursesUseCase,
         private val coursesPrefs: ICoursesOnPrefsUseCases,
         private val guestPrefs: ISetGuestUseCase,
         private val translatorProvider: ITranslateModelProvider
     */
    
    @Published var state: AuthUIState = AuthUIState()
    
    let event = PassthroughSubject<AuthEvent, Never>()
    

//    private let loginUseCase: LoginUseCaseWrapper
//
//    init(loginUseCase: LoginUseCaseWrapper) {
//        self.loginUseCase = loginUseCase
//    }

    func login(email: String, password: String) {
//        loginUseCase.login(
//            email: email,
//            username: email,
//            password: password
//        ) { userId, error in
//            
//            if let error = error {
//                print("Error: \(error)")
//                return
//            }
//
//            if let userId = userId {
//                print("Success: \(userId)")
//            }
//        }
    }
    
      
    func authClicked() {
        state.isLoading = true
        DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
            self.state.isLoading = false
            self.event.send(.goToSelectCourse)
        }
    }
    
    func toggleAuthState() {
        withAnimation(.easeInOut(duration: 0.5)) {
            state.screenState = (state.screenState == .login) ? .register : .login
        }
    }
    
    func onEmailChanged(_ value: String){
        state.email = value
    }
    
    func onPasswordChanged(_ value: String){
        state.password = value
    }


}


