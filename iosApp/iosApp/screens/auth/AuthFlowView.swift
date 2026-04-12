//
//  AuthFlowView.swift
//  iosApp
//
//  Created by  Kseniia Feskova on 08.10.2025.
//

import SwiftUI
import Shared

struct AuthScreen: View {
        
    @StateObject private var viewModel = AuthViewModel()
    
    @State private var showDialog = false
    
    @Namespace private var animation
    
    var goToCourses: () -> Void = { }
    var goToHome: () -> Void = { }
    
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
                    
                    CirclesView(isRegister: viewModel.state.screenState == .register).frame(height: 256)
                    
                    Spacer()
                }.frame(maxWidth: .infinity, maxHeight: .infinity)
                GeometryReader { geo in
                    ZStack {
                        RoundedRectangle(cornerRadius: 24, style: .continuous)
                            .fill(gradient)
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
                                    onGuestClicked: { showDialog = true },
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
                                    onGuestClicked: { showDialog = true },
                                    onLoginClicked: viewModel.toggleAuthState
                                )
                                .transition(.asymmetric(
                                    insertion: .move(edge: .leading),
                                    removal: .move(edge: .trailing)
                                ))
                            }
                        }
                        .animation(.easeInOut(duration: 1.0), value: viewModel.state.screenState)
                    }.frame(height: geo.size.height * 0.75)
                        .frame(maxHeight: .infinity, alignment: .bottom)
                }
            }
            
            
            // Лоадер
            if viewModel.state.isLoading {
                ProgressView().scaleEffect(2)
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(AppColor.background.edgesIgnoringSafeArea(.all))
        .alert("Гостевой режим!", isPresented: $showDialog) {
            Button("Войти") { goToCourses() }
            Button("Отмена", role: .cancel) { }
        } message: {
            Text("Вы заходите как гость, а значит функционал будет ограничен.\nПо желанию, Вы сможете создать аккаунт и сберечь все данные.\nПриятного пользования.")
        }.onReceive(viewModel.event) { event in
            switch event {
            case .goToHome:
                goToHome()
            case .goToSelectCourse:
                goToCourses()
            case .showDialog:
                showDialog = true
            case .showError:
                break
            }
        }
    }
}


struct AuthFlowView_Previews: PreviewProvider {
    static var previews: some View {
        AuthScreen()
    }
}

