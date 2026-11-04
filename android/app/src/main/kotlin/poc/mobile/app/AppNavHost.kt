package poc.mobile.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

/** Point d'entrée de la navigation : les routes des features s'y ajoutent (skill new-feature-module). */
@Composable
fun AppNavHost() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("POC C — Gradle + SPM")
    }
}

@Preview
@Composable
private fun AppNavHostPreview() {
    AppNavHost()
}
