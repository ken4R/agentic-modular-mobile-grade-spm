package com.poc.android.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.poc.auth.AuthIntent
import com.poc.auth.AuthStore
import com.poc.auth.AuthUiState

@Composable
fun AuthScreen(store: AuthStore, modifier: Modifier = Modifier) {
    val state by store.state.collectAsState()
    AuthContent(state = state, onIntent = store::dispatch, modifier = modifier)
}

@Composable
fun AuthContent(state: AuthUiState, onIntent: (AuthIntent) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (state.isLoggedIn) {
            Text("Connecté : ${state.email}", style = MaterialTheme.typography.titleMedium)
            Button(onClick = { onIntent(AuthIntent.Logout) }) { Text("Se déconnecter") }
            return@Column
        }
        OutlinedTextField(
            value = state.email,
            onValueChange = { onIntent(AuthIntent.EmailChanged(it)) },
            label = { Text("E-mail") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(onClick = { onIntent(AuthIntent.Submit) }) { Text("Se connecter") }
        }
    }
}
