package com.example.rickandmortyapplication.data.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Повтор GET при обрыве соединения или 5xx (идемпотентные запросы).
 */
class RetryInterceptor(
    private val maxRetries: Int = 2
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var lastException: IOException? = null

        repeat(maxRetries + 1) { attempt ->
            if (attempt > 0) {
                Thread.sleep(RETRY_DELAY_MS * attempt)
            }
            try {
                val response = chain.proceed(request)
                if (response.isSuccessful || response.code in CLIENT_ERROR_RANGE || attempt == maxRetries) {
                    return response
                }
                response.close()
            } catch (e: IOException) {
                lastException = e
                if (attempt == maxRetries) throw e
            }
        }
        throw lastException ?: IOException("Network request failed")
    }

    private companion object {
        const val RETRY_DELAY_MS = 300L
        val CLIENT_ERROR_RANGE = 400..499
    }
}
