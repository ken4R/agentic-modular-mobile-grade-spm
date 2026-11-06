plugins {
<<<<<<< HEAD
    id("poc.android-app")
}

dependencies {
    implementation(project(":shared:core"))
    implementation(project(":shared:features:auth"))
    implementation(project(":shared:features:catalog"))
    implementation(project(":shared:features:cart"))
    implementation(project(":android:features:auth-ui"))
    implementation(project(":android:features:catalog-ui"))
    implementation(project(":android:features:cart-ui"))
    implementation(libs.koin.android)
=======
    id("android-app")
}

// Composition root : câble les modules, aucune logique (AGENTS.md).
dependencies {
    implementation(project(":shared:core"))
    implementation(project(":shared:features:auth"))
    implementation(project(":android:features:auth"))
    implementation(libs.activity.compose)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.navigation.compose)
    debugImplementation(libs.compose.ui.tooling)
>>>>>>> origin/main
}
