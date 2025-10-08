//
//  iosAppApp.swift
//  iosApp
//
//  Created by  Kseniia Feskova on 08.10.2025.
//

import SwiftUI

@main
struct iosAppApp: App {
    @AppStorage("isLoggedIn") var isLoggedIn = false

    var body: some Scene {
        WindowGroup {
            if isLoggedIn {
               // MainTabView() // экран с 4 табами
            } else {
                AuthFlowView() // выбор языка, логин, регистрация
            }
        }
    }
}
