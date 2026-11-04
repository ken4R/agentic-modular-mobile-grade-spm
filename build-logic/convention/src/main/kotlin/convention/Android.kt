package convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

/** Compose (Jetpack) : versions alignées par le BOM du catalogue, chaque module déclare ses artefacts. */
fun Project.configureAndroidCompose(android: CommonExtension<*, *, *, *, *, *>) {
    android.configureAndroidCommon(this)
    android.buildFeatures.compose = true
    extensions.configure<KotlinAndroidProjectExtension> {
        compilerOptions { jvmTarget.set(JVM_TARGET) }
    }
    dependencies {
        val bom = platform(libs.findLibrary("compose-bom").get())
        add("implementation", bom)
        add("androidTestImplementation", bom)
    }
}
