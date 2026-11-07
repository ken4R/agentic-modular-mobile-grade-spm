plugins {
    id("kmp-feature")
}

kotlin {
    sourceSets.commonMain.dependencies {
        api(project(":shared:features:cart-api"))
        api(project(":shared:core"))
        // Seule dépendance inter-features autorisée : le contrat public de catalog.
        implementation(project(":shared:features:catalog-api"))
    }
}
