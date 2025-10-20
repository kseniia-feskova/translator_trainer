//
//  MarqueeText.swift
//  iosApp
//
//  Created by  Kseniia Feskova on 12.10.2025.
//

import SwiftUI

struct MarqueeText: View {
    let text: String
    let repeatCount: Int = 10
    let speed: Double = 30
    let colors: [Color] = [Color.purple, Color.white]
   
    @State private var offset: CGFloat = 0
    @State private var textWidth: CGFloat = 0
    
    var body: some View {
        GeometryReader { geo in
            HStack(spacing: 0) {
                ForEach(0..<repeatCount, id: \.self) { index in
                    Text(text)
                        .font(.system(size: 28, weight: .bold))
                        .foregroundColor(colors[index % colors.count])
                        .background(
                            GeometryReader { textGeo in
                                Color.clear.onAppear {
                                    // вычисляем ширину текста, чтобы задать длину цикла
                                    textWidth = textGeo.size.width
                                }
                            }
                        )
                }
            }
            .offset(x: offset)
            .onAppear {
                startMarquee(totalWidth: geo.size.width)
            }
            .clipped()
        }
        .frame(height: 40)
    }
    
    private func startMarquee(totalWidth: CGFloat) {
        // сбрасываем начальное положение
        offset = 0
        
        withAnimation(
            Animation.linear(duration: Double(textWidth) / speed)
                .repeatForever(autoreverses: false)
        ) {
            offset = -textWidth * CGFloat(repeatCount / 2)
        }
    }
}


struct MarqueeText_Previews: PreviewProvider {
    static var previews: some View {
        MarqueeText(text: "Login ")
              .background(Color.black)
    }
}
