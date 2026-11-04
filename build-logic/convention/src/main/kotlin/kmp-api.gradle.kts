import convention.configureKmpLibrary

// Contrats publics uniquement (navigation, événements) : pas de dépendances de test imposées.
plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.library")
}

configureKmpLibrary()
