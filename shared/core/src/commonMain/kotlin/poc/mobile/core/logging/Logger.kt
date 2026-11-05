package poc.mobile.core.logging

interface Logger {
    fun debug(tag: String, message: String)
    fun error(tag: String, message: String, throwable: Throwable?)
}

/** Sortie standard : logcat (System.out) sur Android, console Xcode sur iOS. */
class PrintLogger : Logger {
    override fun debug(tag: String, message: String) {
        println("D/$tag: $message")
    }

    override fun error(tag: String, message: String, throwable: Throwable?) {
        println("E/$tag: $message${throwable?.let { " — $it" } ?: ""}")
    }
}
