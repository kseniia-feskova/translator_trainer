//
//  CustomShadowButton.swift
//  iosApp
//
//  Created by  Kseniia Feskova on 20.10.2025.
//

import SwiftUI

struct CustomShadowButton: View {
    var text: String
    var icon: Image? = nil
    var isEnabled: Bool = true
    var onClick: () -> Void = {}

    var body: some View {
        ZStack(alignment: .top) {
            // Тень кнопки (аналог Spacer с фоном в Compose)
            if isEnabled {
                RoundedRectangle(cornerRadius: 30)
                    .fill(AppColor.fieldBorderColor)
                    .frame(height: 48)
                    .offset(y: 5)
            }
            
            Button(action: onClick) {
                HStack {
                    if let icon = icon {
                        icon
                            .resizable()
                            .frame(width: 24, height: 24)
                        Spacer().frame(width: 8)
                    }
                    Text(text)
                        .foregroundColor(isEnabled ? AppColor.dark : AppColor.lightLilaColor)
                        .font(AppTypography.titleSmall) 
                        .padding(.bottom, 2)
                }
                .frame(maxWidth: .infinity, minHeight: 50)
            }
            .background(isEnabled ? AppColor.yellow : AppColor.white)
            .cornerRadius(30)
            .overlay(
                RoundedRectangle(cornerRadius: 30)
                    .stroke(AppColor.fieldBorderColor, lineWidth: 1)
            )
            .disabled(!isEnabled)
        }
    }
}

struct CustomButton_Previews: PreviewProvider {
    static var previews: some View {
        
        VStack
        {
            Spacer()

            CustomShadowButton(
                text: "Login",
                isEnabled: true,
                onClick: {
                    print("Button pressed")
                }
            )
                        
            CustomShadowButton(
                text: "Logout",
                isEnabled: false,
                onClick: {
                    print("Button pressed")
                }
            )
            
            Spacer()

        }
    }
}
