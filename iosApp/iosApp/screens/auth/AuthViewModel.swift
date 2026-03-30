//
//  AuthViewModel.swift
//  iosApp
//
//  Created by  Kseniia Feskova on 08.10.2025.
//

import SwiftUI
import Combine

final class AuthViewModel: ObservableObject {
    
    @Published var state: AuthUIState = AuthUIState()
    let event = PassthroughSubject<AuthEvent, Never>()
      
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
