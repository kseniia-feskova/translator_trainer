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

final class AuthViewModel: ObservableObject {
    @Published var screenState: AuthScreenState = .login
    @Published var email: String = ""
    @Published var password: String = ""
    @Published var isLoading: Bool = false
    @Published var error: String? = nil
    @Published var showGuestDialog: Bool = false

    func toggleAuthState() {
        withAnimation(.easeInOut(duration: 0.5)) {
            screenState = (screenState == .login) ? .register : .login
        }
    }

    func authClicked() {
        isLoading = true
        DispatchQueue.main.asyncAfter(deadline: .now() + 1.2) {
            self.isLoading = false
        }
    }

    func guestSelected() {
        // переход в гостевой режим
        print("Guest selected")
    }
}
