package poc.mobile.feature.auth.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import poc.mobile.feature.auth.AuthIntent
import poc.mobile.feature.auth.AuthStore

/** Écran de connexion : affiche l'état du Store et lui transmet les intents, sans logique propre. */
@Composable
fun AuthScreen(store: AuthStore, modifier: Modifier = Modifier) {
    val state by store.state.collectAsState()
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (state.isAuthenticated) {
            Text("Connecté : ${state.email}", modifier = Modifier.testTag(AuthTags.WELCOME))
            Button(onClick = { store.accept(AuthIntent.Logout) }) { Text("Se déconnecter") }
            return@Column
        }
        OutlinedTextField(
            value = state.email,
            onValueChange = { store.accept(AuthIntent.EmailChanged(it)) },
            label = { Text("E-mail") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.testTag(AuthTags.EMAIL),
        )
        OutlinedTextField(
            value = state.password,
            onValueChange = { store.accept(AuthIntent.PasswordChanged(it)) },
            label = { Text("Mot de passe") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.testTag(AuthTags.PASSWORD),
        )
        state.error?.let { error ->
            Text(error, color = MaterialTheme.colorScheme.error, modifier = Modifier.testTag(AuthTags.ERROR))
        }
        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(onClick = { store.accept(AuthIntent.Submit) }, modifier = Modifier.testTag(AuthTags.SUBMIT)) {
                Text("Se connecter")
            }
        }
    }
}

object AuthTags {
    const val EMAIL = "auth.email"
    const val PASSWORD = "auth.password"
    const val SUBMIT = "auth.submit"
    const val ERROR = "auth.error"
    const val WELCOME = "auth.welcome"
}
