plugins {
    id("kmp-api")
}

kotlin {
    sourceSets.commonMain.dependencies {
        // ProductSummary.priceLabel utilise le formatage partagé de core (feature-api → core).
        api(project(":shared:core"))
    }
}
