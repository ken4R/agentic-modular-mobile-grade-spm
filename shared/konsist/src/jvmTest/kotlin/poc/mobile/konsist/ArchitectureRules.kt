package poc.mobile.konsist

/**
 * Règles d'AGENTS.md : `feature → feature-api → core`, jamais `feature → feature`,
 * `core` indépendant des features, modules UI sans réseau ni base de données.
 *
 * Conventions de paquets : feature `foo-bar` → `poc.mobile.feature.foobar`, son API → `poc.mobile.feature.foobar.api`.
 */
internal object ArchitectureRules {
    const val FEATURE_PACKAGE = "poc.mobile.feature."
    val FORBIDDEN_IN_UI = listOf("io.ktor", "app.cash.sqldelight")
    private val FEATURE_MODULE_PATH = Regex("""/(?:shared|android)/features/([a-z0-9-]+)/""")
    private val PROJECT_DEPENDENCY = Regex("""project\(":shared:features:([a-z0-9-]+)"\)""")

    /** Nom de la feature (sans suffixe `-api`) qui possède ce fichier, ou null hors features. */
    fun featureOf(path: String): String? =
        FEATURE_MODULE_PATH.find(path)?.groupValues?.get(1)?.removeSuffix("-api")

    fun isForeignFeatureImport(import: String, ownFeature: String): Boolean {
        if (!import.startsWith(FEATURE_PACKAGE)) return false
        val segments = import.removePrefix(FEATURE_PACKAGE).split('.')
        val isOwn = segments.first() == ownFeature.replace("-", "")
        val isApi = segments.getOrNull(1) == "api"
        return !isOwn && !isApi
    }

    /** [path] relatif à la racine du dépôt. */
    fun gradleViolations(path: String, script: String): List<String> {
        val owner = featureOf("/$path")
        val isCore = path.startsWith("shared/core/")
        if (owner == null && !isCore) return emptyList()
        return PROJECT_DEPENDENCY.findAll(script)
            .map { it.groupValues[1] }
            .filter { dependency -> isCore || (dependency != owner && !dependency.endsWith("-api")) }
            .map { dependency -> "$path dépend de :shared:features:$dependency" }
            .toList()
    }
}
