package ru.punchman.crossconf24.example

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.raise.Raise

inline fun <A, B, C> Raise<C>.ensure(either: Either<A, B>, raise: (A) -> C): B {
    return ensure(either.mapLeft { raise(it) })
}

inline fun <A, B> Raise<A>.ensure(either: Either<A, B>): B {
    return either.getOrElse { raise(it) }
}

inline fun <A, B> Raise<A>.ensure(action: () -> B, errorHandler: (Throwable) -> A): B = try {
    action()
} catch (t: Throwable) {
    raise(errorHandler(t))
}