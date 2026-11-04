import Core
import SwiftUI

public struct CartView: View {
    @State private var viewModel: CartViewModel

    public init(viewModel: CartViewModel) {
        _viewModel = State(initialValue: viewModel)
    }

    public var body: some View {
        List {
            if viewModel.state.lines.isEmpty {
                Text("Panier vide")
            } else {
                ForEach(viewModel.state.lines, id: \.productId) { line in
                    HStack {
                        Text("\(line.quantity) × \(line.name)")
                        Spacer()
                        Button("Retirer") { viewModel.send(CartIntentRemove(productId: line.productId)) }
                    }
                }
                Text("Total : \(formatPrice(cents: viewModel.state.totalCents))")
                Button("Vider") { viewModel.send(CartIntentClear.shared) }
            }
        }
        .task { await viewModel.observe() }
    }
}
