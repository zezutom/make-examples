package com.tomaszezula.make.plugins

import kotlinx.coroutines.CancellationException

// https://github.com/Kotlin/kotlinx.coroutines/issues/1814
inline fun <R> runSuspendCatching(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (c: CancellationException) {
        throw c
    } catch (e: Throwable) {
        Result.failure(e)
    }
}
