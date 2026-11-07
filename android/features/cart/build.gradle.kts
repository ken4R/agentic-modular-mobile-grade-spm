plugins {
    id("android-feature")
}

dependencies {
    implementation(project(":shared:features:cart"))
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.test.manifest)
}
