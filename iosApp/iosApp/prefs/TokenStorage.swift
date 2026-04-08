//
//  TokenStorage.swift
//  iosApp
//
//  Created by  Kseniia Feskova on 06.04.2026.
//

import Shared
import Foundation
import Security

class TokenStorage: ITokenStorage {

    func saveToken(key: String, token: String) {
        KeychainHelper.save(key: key, value: token)
    }

    func getToken(key: String) -> String? {
        return KeychainHelper.load(key: key)
    }

    func clearToken(key: String) {
        KeychainHelper.delete(key: key)
    }
}

class KeychainHelper { // SharedPreferences in Kotlin

    @discardableResult
    static func save(key: String, value: String) -> Bool {
        let data = value.data(using: .utf8)!

        // Удаляем старое значение (если есть)
        delete(key: key)

        let query: [String: Any] = [
            kSecAttrService as String: Bundle.main.bundleIdentifier ?? "default",
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrAccount as String: key,
            kSecValueData as String: data
        ]

        let status = SecItemAdd(query as CFDictionary, nil)
        return status == errSecSuccess
    }

    static func load(key: String) -> String? {
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrAccount as String: key,
            kSecReturnData as String: true,
            kSecMatchLimit as String: kSecMatchLimitOne
        ]

        var result: AnyObject?

        let status = SecItemCopyMatching(query as CFDictionary, &result)

        guard status == errSecSuccess,
              let data = result as? Data,
              let value = String(data: data, encoding: .utf8) else {
            return nil
        }

        return value
    }

    @discardableResult
    static func delete(key: String) -> Bool {
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrAccount as String: key
        ]

        let status = SecItemDelete(query as CFDictionary)
        return status == errSecSuccess
    }
}
