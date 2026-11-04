plugins {
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
}
