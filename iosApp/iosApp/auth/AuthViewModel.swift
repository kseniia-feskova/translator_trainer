//
//  AuthViewModel.swift
//  iosApp
//
//  Created by  Kseniia Feskova on 08.10.2025.
//

import SwiftUI

enum AuthScreenState {
    case login
    case register
}


struct AuthUIState {
    var email: String = ""
    var password: String = ""
    var error: AuthError?
    var screenState: AuthScreenState = AuthScreenState.login
    var isLoading: Bool = false
}

final class AuthViewModel: ObservableObject {
    
    @Published var state: AuthUIState = AuthUIState()
    @Published var showGuestDialog: Bool = false

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

    func authClicked() {
        print("authClicked, state: \(state)")
        state.isLoading = true
        DispatchQueue.main.asyncAfter(deadline: .now() + 1.2) {
            self.state.isLoading = false
        }
    }

    func onGuestSelected() {
        // переход в гостевой режим
        print("Guest selected")
    }
}
