//
//  AuthFlowView.swift
//  iosApp
//
//  Created by  Kseniia Feskova on 08.10.2025.
//

import SwiftUI
//29b78654

struct AuthFlowView: View {
        @StateObject private var viewModel = AuthViewModel()
        @State private var state = AuthUIState()
        @State private var showDialog = false
        @Namespace private var animation
        
        var body: some View {
            ZStack {
                VStack {
                    Spacer().frame(height: 24)
                    
                    // Верхний бегущий текст (аналог MarqueeText)
                    MarqueeText(
                        //text: state.screenState == .login ? "Login
                        text : "Register "
                    )
                    .frame(height: 40)
                    
                    Spacer().frame(height: 24)
                    
                    // Анимированные круги
//                    Circles(isRegister: if state.screenState == .register {true} else {false})
//                        .frame(height: 150)
                    
                    Spacer().frame(height: 32)
                    
                    // Форма
                    ZStack {
                        if state.screenState == .login {
                            LoginFormView(
                                email: $state.email,
                                password: $state.password,
                                error: $state.error,
                                onLogin: {},
                                onSwitch: { withAnimation { state.screenState = .register } }
                            )
                            .transition(.move(edge: .trailing))
                        } else {
                            RegisterFlowView(
                                email: $state.email,
                                password: $state.password,
                                error: $state.error,
                                onRegister: {},
                                onSwitch: { withAnimation { state.screenState = .login } }
                            )
                            .transition(.move(edge: .leading))
                        }
                    }
                    .padding()
                    .background(
                        RoundedRectangle(cornerRadius: 36)
                            .fill(Color.white)
                            .shadow(radius: 5)
                    )
                    .animation(.easeInOut(duration: 0.8), value: state.screenState)
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
                if state.isLoading {
                    ProgressView().scaleEffect(2)
                }
            }
            .background(Color("BackgroundColor").ignoresSafeArea())
        }
    }


struct AuthFlowView_Previews: PreviewProvider {
    static var previews: some View {
        AuthFlowView()
    }
}

