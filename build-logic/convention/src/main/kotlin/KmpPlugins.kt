import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

private fun Project.configureKmpLibrary() {
    with(pluginManager) {
        apply("org.jetbrains.kotlin.multiplatform")
        apply("com.android.library")
    }
    extensions.configure<KotlinMultiplatformExtension> {
        androidTarget()
        iosArm64()
        iosSimulatorArm64()
        sourceSets.getByName("commonTest").dependencies {
            implementation(kotlin("test"))
            implementation(libs.lib("coroutines-test"))
        }
    }
    extensions.configure<LibraryExtension> { configureAndroidCommon(this) }
    configureDetekt()
}

/** `shared/core` : KMP Android + iOS, SQLDelight. */
class KmpCorePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        configureKmpLibrary()
        pluginManager.apply("app.cash.sqldelight")
    }
}

/**
 * `shared/features/<feature>` et `<feature>-api` : KMP Android + iOS.
 * Les dépendances sont déclarées par chaque module.
 */
class KmpFeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        configureKmpLibrary()
    }
}

/** `shared` : module parapluie qui exporte core et les features dans `Shared.xcframework`, API Swift via SKIE. */
class KmpUmbrellaPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        configureKmpLibrary()
        pluginManager.apply("co.touchlab.skie")
    }
}

/** `shared/konsist` : tests d'architecture Konsist, cible JVM uniquement. */
class KonsistPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.multiplatform")
        extensions.configure<KotlinMultiplatformExtension> {
            jvm()
            sourceSets.getByName("jvmTest").dependencies {
                implementation(kotlin("test"))
                implementation(libs.lib("konsist"))
            }
        }
        configureDetekt()
    }
}
