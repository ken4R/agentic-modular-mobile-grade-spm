plugins {
    `kotlin-dsl`
}

<<<<<<< HEAD
// Les plugins tiers sont sur le classpath de build-logic : les modules les appliquent
// uniquement via les convention plugins ci-dessous, jamais par héritage implicite.
fun plugin(dep: Provider<PluginDependency>): Provider<String> =
    dep.map { "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version.requiredVersion}" }

dependencies {
    implementation(plugin(libs.plugins.kotlin.multiplatform))
    implementation(plugin(libs.plugins.kotlin.android))
    implementation(plugin(libs.plugins.compose.compiler))
    implementation(plugin(libs.plugins.android.application))
    implementation(plugin(libs.plugins.android.library))
    implementation(plugin(libs.plugins.sqldelight))
    implementation(plugin(libs.plugins.skie))
    implementation(plugin(libs.plugins.detekt))
}

gradlePlugin {
    plugins {
        register("kmpCore") {
            id = "poc.kmp-core"
            implementationClass = "KmpCorePlugin"
        }
        register("kmpFeature") {
            id = "poc.kmp-feature"
            implementationClass = "KmpFeaturePlugin"
        }
        register("kmpUmbrella") {
            id = "poc.kmp-umbrella"
            implementationClass = "KmpUmbrellaPlugin"
        }
        register("konsist") {
            id = "poc.konsist"
            implementationClass = "KonsistPlugin"
        }
        register("androidFeature") {
            id = "poc.android-feature"
            implementationClass = "AndroidFeaturePlugin"
        }
        register("androidApp") {
            id = "poc.android-app"
            implementationClass = "AndroidAppPlugin"
        }
    }
=======
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
    implementation(marker(libs.plugins.skie))
>>>>>>> origin/main
}
