import Core
import SwiftUI

/// Écran de connexion : affiche l'état du Store et lui transmet les intents, sans logique propre.
public struct AuthView: View {
    @State private var viewModel: AuthViewModel

    public init(viewModel: AuthViewModel) {
        _viewModel = State(initialValue: viewModel)
    }

    public var body: some View {
        Form {
            if viewModel.state.isAuthenticated {
                Text("Connecté : \(viewModel.state.email)")
                    .accessibilityIdentifier("auth.welcome")
                Button("Se déconnecter") { viewModel.send(AuthIntentLogout.shared) }
            } else {
                TextField("E-mail", text: binding(\.email) { AuthIntentEmailChanged(value: $0) })
                    .textContentType(.emailAddress)
                    .accessibilityIdentifier("auth.email")
                SecureField("Mot de passe", text: binding(\.password) { AuthIntentPasswordChanged(value: $0) })
                    .accessibilityIdentifier("auth.password")
                if let error = viewModel.state.error {
                    Text(error).foregroundStyle(.red).accessibilityIdentifier("auth.error")
                }
                if viewModel.state.isLoading {
                    ProgressView()
                } else {
                    Button("Se connecter") { viewModel.send(AuthIntentSubmit.shared) }
                        .accessibilityIdentifier("auth.submit")
                }
            }
        }
        .task { await viewModel.observe() }
    }

    private func binding(
        _ keyPath: KeyPath<AuthUiState, String>,
        intent: @escaping (String) -> AuthIntent
    ) -> Binding<String> {
        Binding(get: { viewModel.state[keyPath: keyPath] }, set: { viewModel.send(intent($0)) })
    }
}
