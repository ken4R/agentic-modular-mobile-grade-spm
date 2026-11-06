package com.poc.core.logging

interface Logger {
    fun debug(tag: String, message: String)
    fun error(tag: String, message: String, throwable: Throwable? = null)
}

object PrintLogger : Logger {
    override fun debug(tag: String, message: String) = println("D/$tag: $message")

    override fun error(tag: String, message: String, throwable: Throwable?) =
        println("E/$tag: $message${throwable?.let { " — ${it.message}" }.orEmpty()}")
}
