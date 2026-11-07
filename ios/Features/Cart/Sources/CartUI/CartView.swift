import Core
import SwiftUI

/// Panier : affiche l'état du Store et lui transmet les intents, sans logique propre.
public struct CartView: View {
    @State private var viewModel: CartViewModel

    public init(viewModel: CartViewModel) {
        _viewModel = State(initialValue: viewModel)
    }

    public var body: some View {
        List {
            if let error = viewModel.state.error {
                Text(error).foregroundStyle(.red).accessibilityIdentifier("cart.error")
            }
            if viewModel.state.lines.isEmpty {
                Text("Panier vide").accessibilityIdentifier("cart.empty")
            } else {
                ForEach(viewModel.state.lines, id: \.productId) { line in
                    HStack {
                        Text("\(line.name) × \(line.quantity) — \(line.totalLabel)")
                        Spacer()
                        Button("−") { viewModel.send(CartIntentDecrement(productId: line.productId)) }
                        Button("+") { viewModel.send(CartIntentAdd(productId: line.productId)) }
                    }
                    .buttonStyle(.borderless)
                    .accessibilityIdentifier("cart.line.\(line.productId)")
                }
                .onDelete { offsets in
                    offsets.map { viewModel.state.lines[$0].productId }
                        .forEach { viewModel.send(CartIntentRemove(productId: $0)) }
                }
                Text("\(viewModel.state.itemCount) article(s) — \(viewModel.state.totalLabel)")
                    .accessibilityIdentifier("cart.total")
                Button("Vider le panier") { viewModel.send(CartIntentClear.shared) }
                    .accessibilityIdentifier("cart.clear")
            }
        }
        .task { await viewModel.observe() }
    }
}
