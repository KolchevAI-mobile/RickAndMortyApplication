package com.example.rickandmortyapplication.data.paging

import androidx.paging.PagingSource
import kotlin.coroutines.cancellation.CancellationException

/**
 * Одинарная обработка сбоев Paging, согласованная с [com.example.rickandmortyapplication.data.remote.CharacterRemoteMediator].
 */
object PagingLoadResultFactory {
    fun <K : Any, V : Any> fromThrowable(throwable: Throwable): PagingSource.LoadResult<K, V> {
        if (throwable is CancellationException) throw throwable
        return PagingSource.LoadResult.Error(throwable)
    }
}
