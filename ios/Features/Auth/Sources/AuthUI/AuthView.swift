import Core
import SwiftUI

<<<<<<< HEAD
=======
/// Écran de connexion : affiche l'état du Store et lui transmet les intents, sans logique propre.
>>>>>>> origin/main
public struct AuthView: View {
    @State private var viewModel: AuthViewModel

    public init(viewModel: AuthViewModel) {
        _viewModel = State(initialValue: viewModel)
    }

    public var body: some View {
        Form {
<<<<<<< HEAD
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
=======
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
>>>>>>> origin/main
                }
                if viewModel.state.isLoading {
                    ProgressView()
                } else {
                    Button("Se connecter") { viewModel.send(AuthIntentSubmit.shared) }
<<<<<<< HEAD
=======
                        .accessibilityIdentifier("auth.submit")
>>>>>>> origin/main
                }
            }
        }
        .task { await viewModel.observe() }
    }
<<<<<<< HEAD
=======

    private func binding(
        _ keyPath: KeyPath<AuthUiState, String>,
        intent: @escaping (String) -> AuthIntent
    ) -> Binding<String> {
        Binding(get: { viewModel.state[keyPath: keyPath] }, set: { viewModel.send(intent($0)) })
    }
>>>>>>> origin/main
}
