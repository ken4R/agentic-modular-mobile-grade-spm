<<<<<<< HEAD
import AuthUI
import CartUI
import CatalogUI
import Core
import SwiftUI

/// Composition root iOS : démarre le SDK partagé et assemble les trois features, aucune logique métier.
@main
struct PocApp: App {
    private let authViewModel: AuthViewModel
    private let catalogViewModel: CatalogViewModel
    private let cartViewModel: CartViewModel
    private let cartStore: CartStore

    init() {
        SharedSdk.shared.start()
        cartStore = SharedSdk.shared.cartStore()
        authViewModel = AuthViewModel(store: SharedSdk.shared.authStore())
        catalogViewModel = CatalogViewModel(store: SharedSdk.shared.catalogStore())
        cartViewModel = CartViewModel(store: cartStore)
=======
import Core
import SwiftUI

@main
struct PocApp: App {
    init() {
        IosGraph.shared.start()
>>>>>>> origin/main
    }

    var body: some Scene {
        WindowGroup {
<<<<<<< HEAD
            TabView {
                CatalogView(viewModel: catalogViewModel) { [cartStore] productId in
                    cartStore.dispatch(intent: CartIntentAdd(productId: productId))
                }
                .tabItem { Label("Catalogue", systemImage: "list.bullet") }
                CartView(viewModel: cartViewModel)
                    .tabItem { Label("Panier", systemImage: "cart") }
                AuthView(viewModel: authViewModel)
                    .tabItem { Label("Compte", systemImage: "person") }
            }
=======
            RootView()
>>>>>>> origin/main
        }
    }
}
