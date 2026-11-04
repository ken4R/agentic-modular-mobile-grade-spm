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
}

/** `shared/core` : KMP Android + iOS, SQLDelight. */
class KmpCorePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        configureKmpLibrary()
        pluginManager.apply("app.cash.sqldelight")
    }
}

/** `shared/features/<feature>` et `<feature>-api` : KMP Android + iOS. Les dépendances sont déclarées par chaque module. */
class KmpFeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        configureKmpLibrary()
    }
}
