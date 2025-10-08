//
//  AuthFlowView.swift
//  iosApp
//
//  Created by  Kseniia Feskova on 08.10.2025.
//

import SwiftUI

struct AuthFlowView: View {
    @StateObject private var viewModel = AuthViewModel()

    var body: some View {
        ZStack {
            VStack {
                Spacer().frame(height: 24)

                Text(viewModel.screenState == .login ? "Добро пожаловать обратно!" : "Создайте аккаунт")
                    .font(.title)
                    .fontWeight(.bold)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal)

                Spacer().frame(height: 32)

                ZStack {
                    RoundedRectangle(cornerRadius: 36)
                        .fill(Color.white)
                        .shadow(radius: 8)

                    VStack {
                        if viewModel.screenState == .login {
                            LoginFormView(
                                email: $viewModel.email,
                                password: $viewModel.password,
                                onLoginClicked: viewModel.authClicked,
                                onGuestClicked: { viewModel.showGuestDialog = true },
                                onCreateAccountClicked: viewModel.toggleAuthState
                            )
                            .transition(.move(edge: .trailing))
                        } else {
                            RegisterFormView(
                                email: $viewModel.email,
                                password: $viewModel.password,
                                onRegisterClicked: viewModel.authClicked,
                                onGuestClicked: { viewModel.showGuestDialog = true },
                                onLoginClicked: viewModel.toggleAuthState
                            )
                            .transition(.move(edge: .leading))
                        }
                    }
                    .padding()
                    .animation(.easeInOut(duration: 0.6), value: viewModel.screenState)
                }
                .padding(.horizontal, 16)
                .padding(.vertical, 24)
            }

            if viewModel.isLoading {
                ProgressView()
                    .scaleEffect(1.5)
                    .progressViewStyle(CircularProgressViewStyle(tint: .blue))
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
                    .background(Color.black.opacity(0.3))
                    .edgesIgnoringSafeArea(.all)
            }
        }
        .background(Color(.lightGray).ignoresSafeArea())
        .alert("Гостевой режим", isPresented: $viewModel.showGuestDialog) {
            Button("Продолжить", role: .none) { viewModel.guestSelected() }
            Button("Отмена", role: .cancel) { }
        } message: {
            Text("Вы хотите войти как гость?")
        }
    }
}
