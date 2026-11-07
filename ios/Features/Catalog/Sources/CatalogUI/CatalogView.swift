import Core
import SwiftUI

/// Liste des produits : affiche l'état du Store et lui transmet les intents, sans logique propre.
/// `onAddToCart` est fourni par l'app : la vue ne connaît pas la feature cart.
public struct CatalogView: View {
    @State private var viewModel: CatalogViewModel
    private let onAddToCart: (String) -> Void

    public init(viewModel: CatalogViewModel, onAddToCart: @escaping (String) -> Void) {
        _viewModel = State(initialValue: viewModel)
        self.onAddToCart = onAddToCart
    }

    public var body: some View {
        List {
            if viewModel.state.isLoading {
                ProgressView().accessibilityIdentifier("catalog.loading")
            } else if let error = viewModel.state.error {
                Text(error).foregroundStyle(.red).accessibilityIdentifier("catalog.error")
            } else {
                ForEach(viewModel.state.products, id: \.id) { product in
                    HStack {
                        Text("\(product.name) — \(product.priceLabel)")
                            .accessibilityIdentifier("catalog.product.\(product.id)")
                        Spacer()
                        Button("Ajouter") { onAddToCart(product.id) }
                            .accessibilityIdentifier("catalog.add.\(product.id)")
                    }
                }
            }
        }
        .task {
            viewModel.send(CatalogIntentLoad.shared)
            await viewModel.observe()
        }
    }
}
