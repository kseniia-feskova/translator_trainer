//
//  LoginFormView.swift
//  iosApp
//
//  Created by  Kseniia Feskova on 08.10.2025.
//
import SwiftUI

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

    let email: String
    let password: String
    let error: AuthError?
    
    var onEmailChanged: (String) -> Void = { _ in }
    var onPasswordChanged: (String) -> Void = { _ in }
    var onLoginClicked: () -> Void = {}
    var onGuestClicked: () -> Void = {}
    var onCreateAccountClicked: () -> Void = {}
    
    var body: some View {
        VStack {

            Spacer().frame(height: 20)

            Text(String(localized: "login_subtitle"))
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
                    .stroke(error == .emptyFields && email.isEmpty ? Color.red : Color.gray, lineWidth: 1)
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
            
            SecondShadowButton(
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
                Button(action: onCreateAccountClicked) {
                    Text("Create Account")
                        .font(AppTypography.titleSmall)
                        .foregroundColor(AppColor.dark)
                }
                .padding(.bottom, 8)
                
                CustomShadowButton(
                    text: "Login",
                    isEnabled: true,
                    onClick: onLoginClicked 
                    )
                }
            }
        .padding(.horizontal, 20)
    }
}

struct LoginFormView_Previews: PreviewProvider {
    static var previews: some View {
        LoginFormView(
            email: "test", password: "12345", error: nil
        )
    }
}
