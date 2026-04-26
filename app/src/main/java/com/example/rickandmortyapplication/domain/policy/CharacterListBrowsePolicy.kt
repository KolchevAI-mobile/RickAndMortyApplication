package com.example.rickandmortyapplication.domain.policy

import com.example.rickandmortyapplication.domain.model.CharacterFilter

/**
 * Правило: при просмотре полного списка без поиска и фильтров используем
 * локальный кэш + RemoteMediator; иначе — сетевой PagingSource.
 */
object CharacterListBrowsePolicy {
    fun shouldUseLocalCacheWithRemoteMediator(
        query: String,
        filters: CharacterFilter
    ): Boolean =
        query.isBlank() && filters.status == null && filters.gender == null
}
