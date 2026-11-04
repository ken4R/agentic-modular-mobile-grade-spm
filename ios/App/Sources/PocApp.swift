import Core
import SwiftUI

@main
struct PocApp: App {
    init() {
        IosGraph.shared.start()
    }

    var body: some Scene {
        WindowGroup {
            RootView()
        }
    }
}
