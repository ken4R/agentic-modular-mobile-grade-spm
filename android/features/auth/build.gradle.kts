plugins {
<<<<<<< HEAD
    id("poc.android-feature")
=======
    id("android-feature")
>>>>>>> origin/main
}

dependencies {
    implementation(project(":shared:features:auth"))
<<<<<<< HEAD
=======
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.test.manifest)
>>>>>>> origin/main
}
