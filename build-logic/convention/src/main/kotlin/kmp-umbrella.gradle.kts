import convention.configureDetekt
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

// Module parapluie : agrège les modules KMP en un seul framework iOS, exporté vers Swift via SKIE.
// SKIE est chargé ici, avec le plugin Kotlin, pour partager le même classloader.
plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("co.touchlab.skie")
}

extensions.configure<KotlinMultiplatformExtension> {
    iosArm64()
    iosSimulatorArm64()
}

configureDetekt()
