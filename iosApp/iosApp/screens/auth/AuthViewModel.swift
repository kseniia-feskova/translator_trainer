//
//  AuthViewModel.swift
//  iosApp
//
//  Created by  Kseniia Feskova on 08.10.2025.
//

import SwiftUI
import Combine
import Shared

@MainActor
final class AuthViewModel: ObservableObject {
    
    @Published var state: AuthUIState = AuthUIState()

    private lazy var interactor: AuthInteractor = {
        KoinHelper().getAuthInteractor()
    }()
    
    let event = PassthroughSubject<AuthEvent, Never>()
    
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
      
    func handleAuth() {
        guard state.fieldsValid() else {
            state.error = AuthError.emptyFields
            return
        }
            
        state.isLoading = true
            
        switch state.screenState {
        case .login:
            Task { await login() }
        case .register:
            Task { await register() }
        }
    }
    
    func login() async {
        do {
            let result = try await loginAsync(
                email: state.email,
                password: state.password
            )
            
            handle(result: result)
            
        } catch {
            print("Error: \(error)")
            state.isLoading = false
        }
    }
    
    func loginAsync(email: String, password: String) async throws -> AuthResult {
        return try await withCheckedThrowingContinuation { continuation in
            interactor.login(
                email: email,
                password: password
            ) { result, error in
                
                if let result = result {
                    continuation.resume(returning: result)
                } else if let error = error {
                    continuation.resume(throwing: error)
                }
            }
        }
    }
    
    func register() async {
        print("AuthVM, register")
        do {
            let result = try await registerAsync(
                email: state.email,
                password: state.password
            )
            handle(result: result)
        } catch {
            print("Error: \(error)")
            state.isLoading = false
        }
    }
    
    func registerAsync(email: String, password: String) async throws -> AuthResult {
        return try await withCheckedThrowingContinuation { continuation in
            interactor.register(
                email: email,
                password: password
            ) { result, error in
                
                if let result = result {
                    continuation.resume(returning: result)
                } else if let error = error {
                    continuation.resume(throwing: error)
                }
            }
        }
    }
    
    func handleGuest() async {
        do {
            let result = try await handleGuestAsync()
            handle(result: result)
        } catch {
            print("Error: \(error)")
            state.isLoading = false
        }
    }
    
    func handleGuestAsync() async throws -> AuthResult {
        return try await withCheckedThrowingContinuation { continuation in
            interactor.continueAsGuest() { result, error in
                if let result = result {
                    continuation.resume(returning: result)
                } else if let error = error {
                    continuation.resume(throwing: error)
                }
            }
        }
    }
    
    func handle(result: AuthResult) {
        state.isLoading = false
        
        switch result {
            
        case is AuthResult.Success:
            navigateToCourses()
            
        case is AuthResult.NeedsCourseSelection:
            navigateToCourses()
            
        case is AuthResult.NeedsVerification:
            navigateToVerification()
            
        case let error as AuthResult.Error:
            state.error = mapError(error.error)
            
        case is AuthResult.GoToHome:
           // downloadModel(course: home.course) - TODO
            navigateToHome()
            
        default:
            break
        }
    }
    
    func navigateToCourses() {
        print("AuthVM message: navigateToCourses")
        event.send(.goToSelectCourse)
    }

    func navigateToHome() {
        event.send(.goToHome)
    }

    func navigateToVerification() {
       // event.send(.goToVerification) - TODO
    }
    
    func mapError(_ error: BaseAuthError) -> AuthError {
        switch error {
        case .emptyFields: return .emptyFields
        case .wrongPassword: return .wrongPassword
        case .userDoesNotExist: return .userDoesNotExist
        case .emailTaken: return .emailTaken
        case .internetConnectionError: return .internetConnectionError
        default: return .defaultError
        }
    }
    
}

enum AuthError: Error {
    case emptyFields
    case wrongPassword
    case userDoesNotExist
    case emailTaken
    case internetConnectionError
    case defaultError
    
    var message: String {
        switch self {
        case .emptyFields: return "Please fill in all fields"
        case .wrongPassword: return "Incorrect password"
        case .userDoesNotExist: return "User does not exist"
        case .emailTaken: return "User with this e-mail is already exist"
        case .internetConnectionError: return "Check your internet connection"
        case .defaultError: return "Something went wrong"
        }
    }
}
