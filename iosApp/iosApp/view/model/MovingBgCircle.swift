//
//  MovingBgCircle.swift
//  iosApp
//
//  Created by  Kseniia Feskova on 02.04.2026.
//

import SwiftUI

struct MovingCircle: Identifiable {
    let id = UUID()
    var x: CGFloat
    var y: CGFloat
    var angle: CGFloat
    var speed: CGFloat
    var size: CGFloat
    var color: Color

    mutating func update(screen: CGSize, delta: CGFloat) {
        let dx = cos(angle) * speed * delta
        let dy = sin(angle) * speed * delta

        x += dx
        y += dy

        // Отскок от границ
        if x <= 0 || x >= screen.width {
            angle = .pi - angle
        }
        if y <= 0 || y >= screen.height {
            angle = -angle
        }
    }
}

func makeCircles(size: CGSize) -> [MovingCircle] {
    [
        MovingCircle(
            x: .random(in: 0...size.width),
            y: .random(in: 0...size.height),
            angle: .random(in: 0...(.pi * 2)),
            speed: .random(in: 20...200),
            size: size.width * 0.5,
            color: AppColor.dark
        ),
        MovingCircle(
            x: .random(in: 0...size.width),
            y: .random(in: 0...size.height),
            angle: .random(in: 0...(.pi * 2)),
            speed: .random(in: 20...250),
            size: size.width * 0.3,
            color: AppColor.background
        ),
        MovingCircle(
            x: .random(in: 0...size.width),
            y: .random(in: 0...(size.height / 2)),
            angle: .random(in: 0...(.pi * 2)),
            speed: .random(in: 10...150),
            size: size.width * 0.2,
            color: AppColor.yellow
        )
    ]
}
