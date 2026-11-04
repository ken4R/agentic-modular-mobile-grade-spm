import Core
import SwiftUI

public struct AuthView: View {
    @State private var viewModel: AuthViewModel

    public init(viewModel: AuthViewModel) {
        _viewModel = State(initialValue: viewModel)
    }

    public var body: some View {
        Form {
            if viewModel.state.isLoggedIn {
                Text("Connecté : \(viewModel.state.email)")
                Button("Se déconnecter") { viewModel.send(AuthIntentLogout.shared) }
            } else {
                TextField(
                    "E-mail",
                    text: Binding(
                        get: { viewModel.state.email },
                        set: { viewModel.send(AuthIntentEmailChanged(value: $0)) }
                    )
                )
                .textInputAutocapitalization(.never)
                .keyboardType(.emailAddress)
                if let error = viewModel.state.error {
                    Text(error).foregroundStyle(.red)
                }
                if viewModel.state.isLoading {
                    ProgressView()
                } else {
                    Button("Se connecter") { viewModel.send(AuthIntentSubmit.shared) }
                }
            }
        }
        .task { await viewModel.observe() }
    }
}
