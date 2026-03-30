//
//  AuthUIState.swift
//  iosApp
//
//  Created by  Kseniia Feskova on 30.03.2026.
//

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
