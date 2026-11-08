# ADR-0004 — Optimisations de la semaine 2 : où vivent impact, caches et bench

- **Date** : 2026-11-08
- **Statut** : accepté
- **Approche** : C

## Contexte
La semaine 2 (plan-action §2) demande configuration cache validé, cache de build distant, build par impact
(`gradle-dependency-graph`) et cache DerivedData en CI. Contraintes : `ci.yml` est commun aux trois POC et ne
doit pas changer (métrique « durée pipeline PR ») ; `scripts/bench/` appartient au template ; aucun serveur de
cache n'est encore provisionné ; l'agent ne peut lancer ni Gradle (Maven Central en 429, Google Maven bloqué)
ni Xcode localement.

## Options considérées
1. **Graphe d'impact** : plugin `com.vanniktech:gradle-dependency-graph-generator-plugin` (dernière version
   0.8.0, juin 2022, rendu Graphviz) ou tâche maison `projectGraph` dans `build-logic`.
2. **Cache distant** : Develocity, build cache node auto-hébergé, ou `HttpBuildCache` générique piloté par
   variables d'environnement.
3. **Emplacement CI** : modifier `ci.yml` ou ajouter les optimisations dans les workflows propres au POC.

## Décision
- Graphe : tâche `projectGraph` (`convention/ProjectGraph.kt`), appliquée par chaque convention plugin, lue par
  `scripts/affected-modules.sh`. Le plugin vanniktech n'est plus maintenu et sa compatibilité configuration
  cache n'est pas établie ; la tâche maison est le « graphe de dépendances Gradle » demandé, sans dépendance.
- Cache distant : `HttpBuildCache` dans `settings.gradle.kts`, actif si `GRADLE_REMOTE_CACHE_URL` est défini,
  push en CI seulement. Compatible avec un bucket HTTP, un build cache node ou Develocity. Validé en CI contre
  un serveur HTTP minimal (`scripts/http-build-cache.py`) faute de serveur partagé.
- CI : `ci.yml` intact. Build par impact et cache DerivedData dans `tests.yml` (jobs lents, propres au POC),
  validation des caches Gradle dans `perf.yml`, grille de mesure dans `bench.yml` (à la demande).

## Conséquences
- Les jobs émulateur et simulateur ne tournent que si leur plateforme est impactée. `ci.yml` reste un build
  complet : la « durée pipeline PR » mesure toujours le même pipeline dans les trois POC.
- Le cache distant ne sert les développeurs et `ci.yml` qu'une fois un serveur partagé provisionné et ses
  variables passées à `ci.yml` (changement de template).
- Règles d'impact prudentes : tout fichier inconnu, `build-logic/`, `gradle/` ou `.github/` impacte tout ;
  Konsist est impacté par tout fichier Kotlin.
- Critère de réévaluation : ratio de hits mesuré par `bench.yml` et durée des jobs `tests` sur PR.
