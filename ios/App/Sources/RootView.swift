import AuthUI
import Core
import SwiftUI

/// Point d'entrée de la navigation : les vues des features s'y ajoutent (skill new-feature-module).
struct RootView: View {
    @State private var authViewModel = AuthViewModel(store: IosGraph.shared.authStore())

    var body: some View {
        NavigationStack {
            AuthView(viewModel: authViewModel)
                .navigationTitle("Connexion")
        }
    }
}
