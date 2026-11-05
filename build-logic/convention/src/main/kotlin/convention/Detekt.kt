package convention

import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/** detekt sur tout `src/` (le défaut du plugin ignore les source sets KMP). Lancé par `./gradlew detekt`. */
fun Project.configureDetekt() {
    pluginManager.apply("io.gitlab.arturbosch.detekt")
    extensions.configure<DetektExtension> {
        buildUponDefaultConfig = true
        config.setFrom(rootDir.resolve("config/detekt.yml"))
        source.setFrom(files("src"))
    }
}
