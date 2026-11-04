import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

private fun Project.applyComposePlugins(androidPlugin: String) {
    with(pluginManager) {
        apply(androidPlugin)
        apply("org.jetbrains.kotlin.android")
        apply("org.jetbrains.kotlin.plugin.compose")
    }
}

private fun Project.addComposeDependencies() {
    dependencies {
        val bom = platform(libs.lib("compose-bom"))
        add("implementation", bom)
        add("implementation", libs.lib("compose-ui"))
        add("implementation", libs.lib("compose-material3"))
        add("implementation", libs.lib("compose-ui-tooling-preview"))
        add("debugImplementation", libs.lib("compose-ui-tooling"))
    }
}

/** `android/features/<feature>` : écrans Compose, aucune logique réseau ni base de données. */
class AndroidFeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        applyComposePlugins("com.android.library")
        extensions.configure<LibraryExtension> {
            configureAndroidCommon(this)
            buildFeatures.compose = true
        }
        addComposeDependencies()
    }
}

/** `android/app` : composition root. */
class AndroidAppPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        applyComposePlugins("com.android.application")
        extensions.configure<ApplicationExtension> {
            configureAndroidCommon(this)
            buildFeatures.compose = true
            defaultConfig {
                applicationId = "com.poc.mobile"
                targetSdk = Sdk.TARGET
                versionCode = 1
                versionName = "0.1.0"
            }
            packaging.resources.excludes += setOf(
                "/META-INF/{AL2.0,LGPL2.1}",
                "META-INF/versions/9/previous-compilation-data.bin",
            )
        }
        addComposeDependencies()
        dependencies {
            add("implementation", libs.lib("activity-compose"))
        }
    }
}
