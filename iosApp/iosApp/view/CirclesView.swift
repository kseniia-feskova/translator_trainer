//
//  CirclesView.swift
//  iosApp
//
//  Created by  Kseniia Feskova on 20.10.2025.
//

import SwiftUI

struct CirclesView: View {
    var isRegister: Bool

       @State private var offset1: CGSize = .zero
       @State private var offset2: CGSize = .zero
       @State private var offset3: CGSize = .zero

       var body: some View {
           GeometryReader { geo in
               let screenWidth = geo.size.width
               let circleSize = screenWidth / 1.75
               let circleOffset = circleSize * 0.55 // 👈 уменьшили для перекрытия
               let animation = Animation.easeInOut(duration: 1.0)

               ZStack {
                   // Circle 1 — darkColor
                   Circle()
                       .fill(AppColor.dark)
                       .frame(width: circleSize, height: circleSize)
                       .offset(offset1)

                   // Circle 2 — whiteColor
                   Circle()
                       .fill(AppColor.white)
                       .frame(width: circleSize, height: circleSize)
                       .offset(offset2)
                       .zIndex(isRegister ? 1 : 0)

                   // Circle 3 — yellowColor
                   Circle()
                       .fill(AppColor.yellow)
                       .frame(width: circleSize, height: circleSize)
                       .offset(offset3)
               }
               .frame(maxWidth: .infinity, maxHeight: .infinity)
               .blur(radius: 12)
               .onAppear {
                   withAnimation(animation) {
                       updateOffsets(isRegister: isRegister,
                                     screenWidth: screenWidth,
                                     circleOffset: circleOffset)
                   }
               }
               .onChange(of: isRegister) { newValue in
                   withAnimation(animation) {
                       updateOffsets(isRegister: newValue,
                                     screenWidth: screenWidth,
                                     circleOffset: circleOffset)
                   }
               }
           }
           .frame(height: 300)
    }

    private func updateOffsets(isRegister: Bool, screenWidth: CGFloat, circleOffset: CGFloat) {
         // Центр относительно ширины контейнера
         let centerX = screenWidth / 2

         if isRegister {
             // REGISTER state
             offset1 = CGSize(width: circleOffset, height: 0)           // справа
             offset2 = CGSize(width: 0, height: 0)                       // центр
             offset3 = CGSize(width: -circleOffset, height: 0)           // слева
         } else {
             // LOGIN state
             offset1 = CGSize(width: -circleOffset, height: 0)           // слева
             offset2 = CGSize(width: circleOffset, height: 0)            // справа
             offset3 = CGSize(width: 0, height: 0)                       // центр
         }
     }
}

struct CirclesView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            CirclesView(isRegister: false)
                .frame(width: .infinity, height: 300)
                .background(Color.gray.opacity(0.2))
                .previewDisplayName("Login")

            CirclesView(isRegister: true)
                .frame(width: .infinity, height: 300)
                .background(Color.gray.opacity(0.2))
                .previewDisplayName("Register")
        }
        .previewLayout(.sizeThatFits)
    }
}
