//
//  RegisterFormView.swift
//  iosApp
//
//  Created by  Kseniia Feskova on 08.10.2025.
//

import SwiftUI

struct RegisterFormView: View {
    @Binding var email: String
    @Binding var password: String
    var onRegisterClicked: () -> Void
    var onGuestClicked: () -> Void
    var onLoginClicked: () -> Void

    var body: some View {
        VStack(spacing: 16) {
            TextField("Email", text: $email)
                .textFieldStyle(RoundedBorderTextFieldStyle())

            SecureField("Пароль", text: $password)
                .textFieldStyle(RoundedBorderTextFieldStyle())

            Button("Зарегистрироваться", action: onRegisterClicked)
                .buttonStyle(.borderedProminent)

            Button("Войти как гость", action: onGuestClicked)
                .foregroundColor(.gray)

            Button("Уже есть аккаунт? Войти", action: onLoginClicked)
                .foregroundColor(.blue)
        }
    }
}

