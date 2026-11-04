import Core
import SwiftUI

/// `onAddToCart` est fourni par l'app : la vue ne connaît pas la feature cart.
public struct CatalogView: View {
    @State private var viewModel: CatalogViewModel
    private let onAddToCart: (String) -> Void

    public init(viewModel: CatalogViewModel, onAddToCart: @escaping (String) -> Void) {
        _viewModel = State(initialValue: viewModel)
        self.onAddToCart = onAddToCart
    }

    public var body: some View {
        Group {
            if viewModel.state.isLoading {
                ProgressView()
            } else {
                List(viewModel.state.products, id: \.id) { product in
                    HStack {
                        Text("\(product.name) — \(formatPrice(cents: product.priceCents))")
                        Spacer()
                        Button("Ajouter") { onAddToCart(product.id) }
                    }
                }
            }
        }
        .task {
            viewModel.load()
            await viewModel.observe()
        }
    }
}
