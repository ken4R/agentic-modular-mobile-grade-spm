package convention

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/** Cibles communes à tous les modules KMP : Android + iOS Apple Silicon (device et simulateur). */
@OptIn(ExperimentalKotlinGradlePluginApi::class)
fun Project.configureKmpLibrary() {
    extensions.configure<KotlinMultiplatformExtension> {
        androidTarget {
            compilerOptions { jvmTarget.set(JVM_TARGET) }
        }
        iosArm64()
        iosSimulatorArm64()
    }
    extensions.configure<LibraryExtension> {
        configureAndroidCommon(this@configureKmpLibrary)
    }
}

/** Dépendances de test imposées par AGENTS.md (un test par use case et par Store). */
fun Project.configureKmpTests() {
    extensions.configure<KotlinMultiplatformExtension> {
        sourceSets.commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.findLibrary("coroutines-test").get())
        }
    }
}
