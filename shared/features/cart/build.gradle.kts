plugins {
    id("poc.kmp-feature")
}

// cart consomme le contrat public de catalog (feature → feature-api), jamais le module catalog lui-même.
kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":shared:features:cart-api"))
            implementation(project(":shared:features:catalog-api"))
            implementation(project(":shared:core"))
        }
    }
}
