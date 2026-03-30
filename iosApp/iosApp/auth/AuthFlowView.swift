//
//  AuthFlowView.swift
//  iosApp
//
//  Created by  Kseniia Feskova on 08.10.2025.
//

import SwiftUI

struct AuthScreen: View {
    
        @StateObject private var viewModel = AuthViewModel()
    
        @State private var showDialog = false
        @Namespace private var animation
        
        var body: some View {
            ZStack{
                ZStack {
                    VStack {
                        Text(viewModel.state.screenState == .login
                             ? String(localized: "login_title")
                             : String(localized: "register_title"))
                        .foregroundColor(AppColor.dark)
                        .font(AppTypography.displayLarge)
                        .frame(maxWidth: .infinity, alignment: .center)
                                                
                        CirclesView(isRegister: viewModel.state.screenState == .register)
                            .frame(height: 256)
                        
                        Spacer()
                    }.frame(maxWidth: .infinity, maxHeight: .infinity)
                    
                    ZStack {
                        RoundedRectangle(cornerRadius: 24, style: .continuous)
                            .fill(gradient)
                            .padding(.top, 256)
                            .edgesIgnoringSafeArea(.all)
                        Group {
                            if viewModel.state.screenState == .login {
                                LoginFormView(
                                    email: viewModel.state.email,
                                    password: viewModel.state.password,
                                    error: viewModel.state.error,
                                    onEmailChanged: viewModel.onEmailChanged,
                                    onPasswordChanged: viewModel.onPasswordChanged,
                                    onLoginClicked: viewModel.authClicked,
                                    onCreateAccountClicked: viewModel.toggleAuthState,
                                    
                                )
                                    .transition(.asymmetric(
                                        insertion: .move(edge: .trailing),
                                        removal: .move(edge: .leading)
                                    ))
                            } else {
                                RegisterFlowView(
                                    email: viewModel.state.email,
                                    password: viewModel.state.password,
                                    error: viewModel.state.error,
                                    onEmailChanged: viewModel.onEmailChanged,
                                    onPasswordChanged: viewModel.onPasswordChanged,
                                    onRegisterClicked: viewModel.authClicked,
                                    onLoginClicked: viewModel.toggleAuthState
                                )
                                    .transition(.asymmetric(
                                        insertion: .move(edge: .leading),
                                        removal: .move(edge: .trailing)
                                    ))
                            }
                        }
                        .animation(.easeInOut(duration: 1.0), value: viewModel.state.screenState)
                    }
                }
                
                // Диалог
                if showDialog {
                    Color.black.opacity(0.5).ignoresSafeArea()
//                    GuestDialog(
//                        onConfirm: {
//                            showDialog = false
//                            // TODO: guest mode logic
//                        },
//                        onDismiss: { showDialog = false }
//                    )
                }
                
                // Лоадер
                if viewModel.state.isLoading {
                    ProgressView().scaleEffect(2)
                }
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .background(AppColor.background.edgesIgnoringSafeArea(.all))
        }
    }


struct AuthFlowView_Previews: PreviewProvider {
    static var previews: some View {
        AuthScreen()
    }
}

