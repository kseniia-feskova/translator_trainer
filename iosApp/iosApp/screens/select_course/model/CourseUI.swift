//
//  CourseUI.swift
//  iosApp
//
//  Created by  Kseniia Feskova on 02.04.2026.
//

import SwiftUI

struct CourseUI: Codable, Identifiable {
    let id: String
    let originalLanguage: Language
    let translateLanguage: Language
    let originalFlag: String
    let translatedFlag: String
    let allWordsId: String?
    let selectedSetId: String?
    
    enum CodingKeys: String, CodingKey {
           case id
           case originalLanguage = "original_language"
           case translateLanguage = "translate_language"
           case originalFlag = "original_flag"
           case translatedFlag = "translated_flag"
           case allWordsId = "all_words_id"
           case selectedSetId = "selected_set_id"
       }
}

enum Language: String, Codable {
    case english = "en"
    case french = "fr"
    case german = "de"
    case russian = "ru"
}

extension Language {
    
    var localized: String {
        switch self {
        case .german:
            return NSLocalizedString("german", comment: "")
        case .russian:
            return NSLocalizedString("russian", comment: "")
        case .french:
            return NSLocalizedString("french", comment: "")
        case .english:
            return NSLocalizedString("english", comment: "")
        }
    }
}
