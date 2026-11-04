plugins {
    id("android-app")
}

// Composition root : câble les modules, aucune logique (AGENTS.md).
dependencies {
    implementation(project(":shared:core"))
    implementation(libs.activity.compose)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    debugImplementation(libs.compose.ui.tooling)
}
