//
//  LoginFormView.swift
//  iosApp
//
//  Created by  Kseniia Feskova on 08.10.2025.
//
import SwiftUI

struct LoginFormView: View {
    @Binding var email: String
    @Binding var password: String
    var onLoginClicked: () -> Void
    var onGuestClicked: () -> Void
    var onCreateAccountClicked: () -> Void

    var body: some View {
        VStack(spacing: 16) {
            TextField("Email", text: $email)
                .textFieldStyle(RoundedBorderTextFieldStyle())

            SecureField("Пароль", text: $password)
                .textFieldStyle(RoundedBorderTextFieldStyle())

            Button("Войти", action: onLoginClicked)
                .buttonStyle(.borderedProminent)

            Button("Войти как гость", action: onGuestClicked)
                .foregroundColor(.gray)

            Button("Создать аккаунт", action: onCreateAccountClicked)
                .foregroundColor(.blue)
        }
    }
}
