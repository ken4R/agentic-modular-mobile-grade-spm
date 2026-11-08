import convention.JVM_TARGET
import convention.libs
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import convention.configureProjectGraph

// Tests d'architecture : JVM seul, lancés par `./gradlew :shared:konsist:allTests`.
plugins {
    id("org.jetbrains.kotlin.multiplatform")
}

extensions.configure<KotlinMultiplatformExtension> {
    jvm {
        compilerOptions { jvmTarget.set(JVM_TARGET) }
    }
    sourceSets.named("jvmTest") {
        dependencies {
            implementation(kotlin("test"))
            implementation(libs.findLibrary("konsist").get())
        }
    }
}

// Les règles lisent tout le dépôt : sans ces entrées, le test resterait à jour (ou en cache) après une violation.
tasks.named<Test>("jvmTest") {
    val root = rootDir
    systemProperty("projectRoot", root.absolutePath)
    inputs.files(
        fileTree(root) {
            include("shared/**", "android/**")
            include("**/*.kt", "**/*.kts")
            exclude("**/build/**")
        },
    ).withPathSensitivity(PathSensitivity.RELATIVE)
}
configureProjectGraph("allTests")
