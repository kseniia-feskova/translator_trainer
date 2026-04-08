//
//  IOSWordDao.swift
//  iosApp
//
//  Created by  Kseniia Feskova on 06.04.2026.
//


import Foundation
import SQLite
import Shared

class LocalDatabase: ILocalDatabase {

    private let dao: IOSAppDao

    init() throws {
        self.dao = try IOSAppDao()
    }
    
    func clearDatabase() {
        do {
            try dao.clearUsers()
            try dao.clearWords()
            try dao.clearSets()
        }
        catch {
            print("ClearDatabase error: \(error)")
        }
    }
    
}

class IOSAppDao {

    private let db: Connection

    private let words = Table("words")
    private let setsOfWords = Table("sets_of_words")
    private let setWordCrossRef = Table("set_word_cross_ref")

    init() throws{
        let path = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true).first!
        db = try! Connection("\(path)/app_database.sqlite3")

        // Создание таблиц
        try db.run(words.create(ifNotExists: true) { t in
            t.column(Expression<String>("id"), primaryKey: true)
            t.column(Expression<String>("word"))
        })

        try db.run(setsOfWords.create(ifNotExists: true) { t in
            t.column(Expression<String>("id"), primaryKey: true)
            t.column(Expression<String>("name"))
        })

        try db.run(setWordCrossRef.create(ifNotExists: true) { t in
            t.column(Expression<String>("setId"))
            t.column(Expression<String>("wordId"))
        })
    }

    func clearWords() throws {
        try db.run(words.delete())
    }

    func clearUsers() throws {
        try db.run(setsOfWords.delete())
    }

    func clearSets() throws {
        try db.run(setWordCrossRef.delete())
    }
}
