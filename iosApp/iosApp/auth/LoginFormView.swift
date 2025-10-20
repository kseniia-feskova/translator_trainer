//
//  LoginFormView.swift
//  iosApp
//
//  Created by  Kseniia Feskova on 08.10.2025.
//
import SwiftUI

struct AuthUIState {
    var email: String = ""
    var password: String = ""
    var error: AuthError?
}

enum AuthError: Error {
    case emptyFields
    case wrongPassword
    
    var message: String {
        switch self {
        case .emptyFields: return "Please fill in all fields"
        case .wrongPassword: return "Incorrect password"
        }
    }
}

struct LoginFormView: View {
    @State private var passwordVisible = false
    @State private var state = AuthUIState()
    
    var onEmailChanged: (String) -> Void = { _ in }
    var onPasswordChanged: (String) -> Void = { _ in }
    var onLoginClicked: () -> Void = {}
    var onGuestClicked: () -> Void = {}
    var onCreateAccountClicked: () -> Void = {}
    
    var body: some View {
        VStack {
            Spacer(minLength: 20)
            
            Text("Welcome back")
                .font(.system(size: 22, weight: .bold))
                .foregroundColor(Color.blue)
                .frame(maxWidth: .infinity, alignment: .center)
            
            Spacer().frame(height: 16)
            
            // Email field
            TextField("Email", text: Binding(
                get: { state.email },
                set: {
                    state.email = $0
                    onEmailChanged($0)
                }
            ))
            .padding()
            .background(Color(UIColor.secondarySystemBackground))
            .cornerRadius(8)
            .keyboardType(.emailAddress)
            .textInputAutocapitalization(.never)
            .overlay(
                RoundedRectangle(cornerRadius: 8)
                    .stroke(state.error == .emptyFields && state.email.isEmpty ? Color.red : Color.gray, lineWidth: 1)
            )
            
            Spacer().frame(height: 16)
            
            // Password field
            HStack {
                if passwordVisible {
                    TextField("Password", text: Binding(
                        get: { state.password },
                        set: {
                            state.password = $0
                            onPasswordChanged($0)
                        }
                    ))
                } else {
                    SecureField("Password", text: Binding(
                        get: { state.password },
                        set: {
                            state.password = $0
                            onPasswordChanged($0)
                        }
                    ))
                }
                
                Button(action: {
                    passwordVisible.toggle()
                }) {
                    Image(systemName: passwordVisible ? "eye" : "eye.slash")
                        .foregroundColor(.gray)
                }
            }
            .padding()
            .background(Color(UIColor.secondarySystemBackground))
            .cornerRadius(8)
            .overlay(
                RoundedRectangle(cornerRadius: 8)
                    .stroke(
                        (state.error == .emptyFields && state.password.isEmpty) || state.error == .wrongPassword ? Color.red : Color.gray,
                        lineWidth: 1
                    )
            )
            
            Spacer().frame(height: 8)
            
            HStack {
                Spacer()
                Text("Forgot password?")
                    .font(.footnote)
                    .foregroundColor(.blue)
            }
            
            if let error = state.error {
                Text(error.message)
                    .foregroundColor(.red)
                    .font(.footnote)
                    .frame(maxWidth: .infinity, alignment: .center)
            } else {
                Spacer().frame(height: 8)
            }
            
            Text("or")
                .foregroundColor(.gray)
                .padding(.vertical, 16)
            
//            Button("Continue as Guest", action: onGuestClicked)
//                .padding()
//                .frame(maxWidth: .infinity)
//                .background(Color.gray.opacity(0.2))
//                .cornerRadius(8)
            
//            Button {
//                // Google sign in
//            } label: {
//                HStack {
//                    Image(systemName: "g.circle.fill")
//                    Text("Continue with Google")
//                }
//                .padding()
//                .frame(maxWidth: .infinity)
//                .background(Color.white)
//                .cornerRadius(8)
//                .shadow(radius: 3)
//            }
            
            Spacer()
            
            VStack {
                Button(action: onCreateAccountClicked) {
                    Text("Create Account")
                        .fontWeight(.bold)
                        .foregroundColor(.blue)
                        .font(.system(size: 16))
                }
                .padding(.bottom, 8)
                
                Button(action: onLoginClicked) {
                    Text("Login")
                        .fontWeight(.semibold)
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(Color.blue)
                        .foregroundColor(.white)
                        .cornerRadius(12)
                        .shadow(radius: 4)
                }
            }
        }
        .padding(.horizontal, 20)
    }
}

struct LoginFormView_Previews: PreviewProvider {
    static var previews: some View {
        LoginFormView()
    }
}
