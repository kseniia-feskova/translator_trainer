//
//  iosAppApp.swift
//  iosApp
//
//  Created by  Kseniia Feskova on 08.10.2025.
//

import SwiftUI
import Shared

final class AppState: ObservableObject {
    @Published var isAuthorized: Bool = false
}

@main
struct iosAppApp: App {
    @AppStorage("isLoggedIn") var isLoggedIn = false
    
    init() {
        do {
            let storage = TokenStorage()
            let database = try LocalDatabase()
            let dataStorage = DataStoreManagerIOS()
            KoinKt.doInitKoin(tokenStorage: storage, localDataBase: database, dataStorageManager : dataStorage)
        } catch {
            print("Database error: \(error)")
        }
     }
        
    var body: some Scene {
        WindowGroup { RootView() }
    }
}


struct RootView: View {
    
    @StateObject var appState = AppState()
    
    var body: some View {
        Group {
            if appState.isAuthorized {
               // AuthorizedAppView()
            } else {
                UnauthorizedAppView(appState: appState)
            }
        }
    }
}

enum AuthRoute: Hashable {
    case splash
    case auth
    case selectCourse
}

struct UnauthorizedAppView: View {
    
    @ObservedObject var appState: AppState
    
    @State private var path: [AuthRoute] = []
    
    var body: some View {
        NavigationStack(path: $path) {
            
            SplashView(
                onFinished: {
                    path.append(.auth)
                }
            )
            
            .navigationDestination(for: AuthRoute.self) { route in
                switch route {
                
                case .auth:
                    AuthScreen()
                
                case .splash:
                    EmptyView()
                    
                case .selectCourse:
                    SelecteCourseScreen()
                }
            }
        }
    }
}
