//
//  GuestModeDialog.swift
//  iosApp
//
//  Created by  Kseniia Feskova on 30.03.2026.
//

import SwiftUI

struct GuestModeDialog: View {
    
    var onConfirm: () -> Void
    var onDismiss: () -> Void
    
    var body: some View {
        VStack(spacing: 16) {
            
            Text("Гостевой режим!")
                .font(.title2)
            
            Text("Вы заходите как гость, а значит функционал будет ограничен.\nПо желанию, Вы сможете создать аккаунт и сберечь все данные.\nПриятного пользования.")
                .font(.body)
            
            Button("Войти") {
                onConfirm()
            }
            
            Button("Отмена") {
                onDismiss()
            }
        }
        .padding()
    }
}
