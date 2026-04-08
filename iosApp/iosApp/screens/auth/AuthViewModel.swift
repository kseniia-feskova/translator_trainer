//
//  AuthViewModel.swift
//  iosApp
//
//  Created by  Kseniia Feskova on 08.10.2025.
//


/*
 private val login: ILoginUseCase,
     private val register: IRegisterUseCase,
     private val registerByFirebase: IRegisterWithFirebaseUseCase,
     private val getCourses: IGetAllCoursesUseCase,
     private val coursesPrefs: ICoursesOnPrefsUseCases,
     private val guestPrefs: ISetGuestUseCase,
     private val translatorProvider: ITranslateModelProvider
 */

import SwiftUI
import Combine
import Shared

final class AuthViewModel: ObservableObject {
    
    private let loginUseCase: ILoginUseCase = Shared.KoinHelper().getLoginUseCase()
    
    
    @Published var state: AuthUIState = AuthUIState()
    
    let event = PassthroughSubject<AuthEvent, Never>()
      
    func authClicked() {
        if(self.state.fieldsValid()){
            state.isLoading = true
            switch(state.screenState){
            case .login: login()
            case .register: register()
            }
        }
        DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
            self.state.isLoading = false
            self.event.send(.goToSelectCourse)
        }
    }
    
    func login() {
        loginUseCase.invoke(
            email: state.email,
            username: state.email,
            password: state.password
        ) { userId, error in
            
            if let error = error {
                print("Error: \(error)")
                return
            }
            
            if(error != nil) {
                self.handlerError()
            }

            if let userId = userId {
                print("Success: \(userId)")
            }
        }
    }
    
    
    /**
     private fun handleAuth(
            goToCourses: () -> Unit,
            goToHome: () -> Unit,
            goToVerification: () -> Unit
        ) {
            val state = _uiState.value
            if (state.fieldsValid()) {
                _uiState.update { it.copy(isLoading = true) }
                when (state.screenState) {
                    AuthScreenState.LOGIN -> login(state, goToCourses, goToHome)
                    AuthScreenState.REGISTER -> register(state, goToCourses, goToVerification)
                }
            } else {
                _uiState.update { it.copy(error = AuthError.EMPTY_FIELDS) }
            }
            _uiState.update { it.copy(isLoading = false) }
        }*/
    
    func register(){
    
    }
    
    /**
     private fun <T> handleError(response: Result<T>) {
            val error = response.exceptionOrNull()
            val errorMsg = when (error?.message) {
                "Wrong password" -> AuthError.WRONG_PASSWORD
                "User does not exist" -> AuthError.USER_DOES_NOT_EXIST
                "User already exists" -> AuthError.EMAIL_TAKEN
                "Failed to connect" -> AuthError.INTERNET_CONNECTION_ERROR
                else -> AuthError.DEFAULT
            }
            _uiState.update { it.copy(error = errorMsg, isLoading = false) }
        }
     */
    
    func handlerError(){
        
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


