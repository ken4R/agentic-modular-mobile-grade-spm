package com.poc.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

/** Moteur HTTP de la plateforme (OkHttp sur Android, Darwin sur iOS). */
expect fun httpClientEngine(): HttpClientEngine

fun createHttpClient(engine: HttpClientEngine = httpClientEngine()): HttpClient =
    HttpClient(engine) {
        install(ContentNegotiation) {
            json()
        }
    }
