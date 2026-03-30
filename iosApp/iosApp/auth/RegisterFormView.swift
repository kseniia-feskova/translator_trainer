//
//  RegisterFormView.swift
//  iosApp
//
//  Created by  Kseniia Feskova on 08.10.2025.
//

import SwiftUI

struct RegisterFlowView: View {
    
    @State private var passwordVisible = false
    
    let email: String
    let password: String
    let error: AuthError?
    
    var onEmailChanged: (String) -> Void = { _ in }
    var onPasswordChanged: (String) -> Void = { _ in }
    var onRegisterClicked: () -> Void = {}
    var onGuestClicked: () -> Void = {}
    var onLoginClicked: () -> Void = {}
    
    var body: some View {
        VStack {
            Spacer().frame(height: 20)
            
            Text(String(localized: "signup_subtitle"))
                .foregroundColor(AppColor.dark)
                .font(AppTypography.displayMedium)
                .frame(maxWidth: .infinity, alignment: .center)
    
            
            Spacer().frame(height: 16)
            
            // Email field
            TextField("Email", text: Binding(
                get: { email },
                set: { onEmailChanged($0) }
            ))
            .padding()
            .background(Color(UIColor.secondarySystemBackground))
            .cornerRadius(8)
            .keyboardType(.emailAddress)
            .textInputAutocapitalization(.never)
            .overlay(
                RoundedRectangle(cornerRadius: 8)
                    .stroke(error == .emptyFields && email.isEmpty ? Color.red : Color.gray,lineWidth: 1)
            )
            
            Spacer().frame(height: 16)
            
            // Password field
            HStack {
                if passwordVisible {
                    TextField("Password", text: Binding(
                        get: { password },
                        set: { onPasswordChanged($0) }
                    ))
                } else {
                    SecureField("Password", text: Binding(
                        get: { password },
                        set: { onPasswordChanged($0) }
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
                        (error == .emptyFields && password.isEmpty) || error == .wrongPassword ? Color.red : Color.gray,
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
            
            if let error = error {
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
            
            CustomShadowButton(
                text: "Сontinue as Guest",
                isEnabled: true,
                onClick: onGuestClicked
            )
            
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
                Button(action: onLoginClicked) {
                    Text("Login with account")
                        .font(AppTypography.titleSmall)
                        .foregroundColor(AppColor.dark)
                }
                .padding(.bottom, 8)
                
                CustomShadowButton(
                    text: "Sign up",
                    isEnabled: true,
                    onClick: onRegisterClicked
                )
            }
        }
        .padding(.horizontal, 20)
    }
}

struct RegisterFlowView_Previews: PreviewProvider {
    static var previews: some View {
        RegisterFlowView(
            email: "test", password: "12345", error: nil
        )
    }
}

