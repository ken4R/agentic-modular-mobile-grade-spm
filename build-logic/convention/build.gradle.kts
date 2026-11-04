plugins {
    `kotlin-dsl`
}

// Les plugins sont référencés via leur artefact « marker » : les versions restent celles du catalogue.
fun marker(plugin: Provider<PluginDependency>) =
    plugin.map { "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version.requiredVersion}" }

dependencies {
    implementation(marker(libs.plugins.kotlin.multiplatform))
    implementation(marker(libs.plugins.kotlin.android))
    implementation(marker(libs.plugins.compose.compiler))
    implementation(marker(libs.plugins.android.library))
    implementation(marker(libs.plugins.android.application))
    implementation(marker(libs.plugins.detekt))
}
