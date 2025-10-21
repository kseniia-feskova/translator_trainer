//
//  AppColor.swift
//  iosApp
//
//  Created by  Kseniia Feskova on 20.10.2025.
//
import SwiftUI

enum AppColor {
    static let background = Color("bgColor")
    static let dark = Color("darkColor")
    static let yellow = Color("yellowColor")
    static let white = Color("whiteColor")
    static let fieldBorderColor = Color("fieldBorderColor")
    static let lightLilaColor = Color("lightLilaColor")

}


let gradient = LinearGradient(
    gradient: Gradient(colors: [
        Color("gradientLightColor"),
        Color("gradientMediumColor"),
        Color("gradientDarkColor")
    ]),
    startPoint: .top,
    endPoint: .bottom
)

