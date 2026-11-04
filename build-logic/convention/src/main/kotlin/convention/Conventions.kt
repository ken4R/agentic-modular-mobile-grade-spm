package convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

// Niveaux d'API Android : AGP 8.9 supporte au plus l'API 35.
const val COMPILE_SDK = 35
const val TARGET_SDK = 35
const val MIN_SDK = 26
val JVM_TARGET = JvmTarget.JVM_17
val JAVA_VERSION = JavaVersion.VERSION_17

val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

/** `:shared:features:cart-api` → `poc.mobile.shared.features.cart.api`. */
fun Project.pocNamespace(): String =
    "poc.mobile" + path.replace(':', '.').replace('-', '.')

fun CommonExtension<*, *, *, *, *, *>.configureAndroidCommon(project: Project) {
    namespace = project.pocNamespace()
    compileSdk = COMPILE_SDK
    defaultConfig {
        minSdk = MIN_SDK
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JAVA_VERSION
        targetCompatibility = JAVA_VERSION
    }
}
