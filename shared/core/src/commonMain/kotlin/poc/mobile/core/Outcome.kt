package poc.mobile.core

/** Remplace `Result<T>`, non exporté vers Swift. Devient un enum Swift exhaustif via SKIE. */
sealed class Outcome<out T> {
    data class Success<out T>(val value: T) : Outcome<T>()
    data class Failure(val message: String) : Outcome<Nothing>()
}

inline fun <T, R> Outcome<T>.map(transform: (T) -> R): Outcome<R> = when (this) {
    is Outcome.Success -> Outcome.Success(transform(value))
    is Outcome.Failure -> this
}
