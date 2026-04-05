//
//  SelectCourseView.swift
//  iosApp
//
//  Created by  Kseniia Feskova on 30.03.2026.
//

import SwiftUI

private let testCourse = CourseUI(
    id: "1",
    originalLanguage: .english,
    translateLanguage: .german,
    originalFlag: Language.english.rawValue,
    translatedFlag: Language.german.rawValue,
    allWordsId: "5",
    selectedSetId: "6"
)

struct SelecteCourseScreen: View {
    
    @StateObject private var viewModel = SelectCourseViewModel()
    var goToHome: () -> Void = { }
    
    var body: some View {
        ZStack{
            
            BackgroundDecorAnimated()

            VStack{
                Text("Before we start")
                .foregroundColor(AppColor.dark)
                .font(AppTypography.displayLarge)
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.horizontal, 18)
                .padding(.vertical, 24)
                
                Spacer()
                GeometryReader { geo in
                    ZStack {
                        RoundedRectangle(cornerRadius: 36, style: .continuous)
                            .fill(gradient)
                            .edgesIgnoringSafeArea(.all)
                        Group {
                            CourseSelectionView(
                                courses: [testCourse],
                                selectedCourse: nil
                            )
                        }
                    }.frame(height: geo.size.height * 0.65)
                        .frame(maxHeight: .infinity, alignment: .bottom)
                }
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)

        }
    }
}

struct SelecteCourseScreen_Previews: PreviewProvider {
    static var previews: some View {
        SelecteCourseScreen()
    }
}


struct CourseSelectionView: View {
    
    let courses: [CourseUI]
    let selectedCourse: CourseUI?
    let onSelect: (CourseUI) -> Void = {_ in }
    let onBack: () -> Void = {}
    let onContinue: () -> Void = {}
    
    var body: some View {
        
        VStack(spacing: 0) {
            // 🔹 Title
            Text("Select course")
                .font(AppTypography.displayMedium)
                .foregroundColor(AppColor.dark)
                .frame(maxWidth: .infinity, alignment: .center)
            
            Spacer().frame(height: 16)
            
            // 🔹 Контент (список + кнопки)
            ZStack(alignment: .bottom) {
                
                // 📜 Scroll
                ScrollView {
                    LazyVStack(spacing: 12) {
                        ForEach(courses, id: \.id) { course in               CourseItemView(
                            course: course,
                            isSelected: course.id == selectedCourse?.id,
                            onSelect: onSelect
                            )
                        }
                    }
                    .padding(.bottom, 120)
                }
                
                VStack(spacing: 16) {
                    
                    Text("Back")
                        .font(.system(size: 16, weight: .bold))
                        .foregroundColor(.dark)
                        .onTapGesture {
                            onBack()
                        }
                    
                    
                    CustomShadowButton(
                        text: "Continue",
                        isEnabled: true,
                        onClick: onContinue
                    )
                }
                .frame(maxWidth: .infinity)
            }
        } .padding(.all, 16)
    }
}


struct CourseItemView: View {

    let course: CourseUI
    let isSelected: Bool
    let onSelect: (CourseUI) -> Void

    var body: some View {
        HStack(spacing: 8) {

            CustomRadioButton(
                isSelected: isSelected,
                onTap: { onSelect(course) }
            )
            .frame(width: 32, height: 32)
           
            Text("\(course.originalLanguage.localized) - \(course.translateLanguage.localized)")                .foregroundColor(.dark)
                .font(.system(
                    size: 16,
                    weight: isSelected ? .bold : .semibold
                ))

            Spacer()

            FlagView(imageName: course.originalFlag)
            FlagView(imageName: course.translatedFlag)
        }
        .padding(.vertical, 10)
        .frame(maxWidth: .infinity, alignment: .leading)
        .contentShape(Rectangle()) // ❗ важно для клика по всей строке
        .onTapGesture {
            onSelect(course)
        }
    }
}

struct CustomRadioButton: View {

    let isSelected: Bool
    let onTap: () -> Void

    var body: some View {
        ZStack {
            Circle().stroke(Color.dark, lineWidth: 2)
            if isSelected {
                Circle()
                    .fill(Color.dark)
                    .padding(6)
            }
        }
        .padding(.horizontal, 4)
        .onTapGesture {
            onTap()
        }
    }
}

struct FlagView: View {

    let imageName: String

    var body: some View {
        Image(imageName)
            .resizable()
            .scaledToFit()
            .frame(height: 32)
            .background(
                RoundedRectangle(cornerRadius: 8)
                    .stroke(Color.gray, lineWidth: 1)
            )
    }
}
