import AuthUI
import CatalogUI
import Core
import SwiftUI

/// Point d'entrée de la navigation : un onglet par feature (skill new-feature-module).
struct RootView: View {
    @State private var authViewModel = AuthViewModel(store: IosGraph.shared.authStore())
    @State private var catalogViewModel = CatalogViewModel(store: IosGraph.shared.catalogStore())

    var body: some View {
        TabView {
            NavigationStack {
                AuthView(viewModel: authViewModel)
                    .navigationTitle("Connexion")
            }
            .tabItem { Label("Compte", systemImage: "person") }
            NavigationStack {
                CatalogView(viewModel: catalogViewModel, onAddToCart: { _ in })
                    .navigationTitle("Catalogue")
            }
            .tabItem { Label("Catalogue", systemImage: "list.bullet") }
        }
    }
}
