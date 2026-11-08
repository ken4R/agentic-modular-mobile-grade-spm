package convention

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.register
import org.gradle.work.DisableCachingByDefault

/**
 * Graphe des dépendances entre projets, lu par `scripts/affected-modules.sh` (build par impact).
 * Sortie, une ligne par fait :
 * - `module <chemin Gradle> <dossier relatif> <tâche de tests ou ->`
 * - `dep <chemin Gradle> <chemin Gradle de la dépendance>`
 * Compatible configuration cache : les valeurs sont calculées à la configuration, l'action ne lit pas `project`.
 */
@DisableCachingByDefault(because = "Affiche le graphe, aucune sortie de fichier")
abstract class ProjectGraphTask : DefaultTask() {
    @get:Input abstract val modulePath: Property<String>

    @get:Input abstract val moduleDir: Property<String>

    @get:Input abstract val testTask: Property<String>

    @get:Input abstract val projectDependencies: ListProperty<String>

    @TaskAction
    fun printGraph() {
        val path = modulePath.get()
        println("module $path ${moduleDir.get()} ${testTask.get()}")
        projectDependencies.get().forEach { println("dep $path $it") }
    }
}

/** Enregistre `projectGraph` ; `unitTestTask` = tâche des tests unitaires du module, `-` si aucune. */
fun Project.configureProjectGraph(unitTestTask: String) {
    val project = this
    tasks.register<ProjectGraphTask>("projectGraph") {
        group = "help"
        description = "Affiche les dépendances entre projets (build par impact)."
        modulePath.set(project.path)
        moduleDir.set(project.rootDir.toPath().relativize(project.projectDir.toPath()).toString())
        testTask.set(unitTestTask)
        // Toutes les configurations : une dépendance de test ou d'export rend aussi le module impacté.
        projectDependencies.set(
            project.provider {
                project.configurations
                    .flatMap { it.dependencies.withType(ProjectDependency::class.java) }
                    .map { it.path }
                    .filter { it != project.path }
                    .distinct()
                    .sorted()
            },
        )
    }
}
