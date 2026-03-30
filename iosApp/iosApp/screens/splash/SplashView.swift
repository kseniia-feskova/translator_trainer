//
//  SplashView.swift
//  iosApp
//
//  Created by  Kseniia Feskova on 30.03.2026.
//

import SwiftUI

struct SplashView: View {
    
    var onFinished: () -> Void
    
    var body: some View {
        Text("Splash")
            .onAppear {
                DispatchQueue.main.asyncAfter(deadline: .now() + 1.5) {
                    onFinished()
                }
            }
    }
}
