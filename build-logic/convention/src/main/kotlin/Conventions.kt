import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

internal object Sdk {
    const val COMPILE = 35
    const val TARGET = 35
    const val MIN = 26
}

internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun VersionCatalog.lib(alias: String) = findLibrary(alias).get()

/** Namespace Android dérivé du chemin Gradle : `:shared:features:auth-api` → `com.poc.shared.features.auth_api`. */
internal val Project.pocNamespace: String
    get() = "com.poc." + path.removePrefix(":").replace(':', '.').replace('-', '_')

internal fun Project.configureAndroidCommon(android: CommonExtension<*, *, *, *, *, *>) {
    android.apply {
        namespace = pocNamespace
        compileSdk = Sdk.COMPILE
        defaultConfig.minSdk = Sdk.MIN
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }
    tasks.withType<KotlinJvmCompile>().configureEach {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
    }
}
