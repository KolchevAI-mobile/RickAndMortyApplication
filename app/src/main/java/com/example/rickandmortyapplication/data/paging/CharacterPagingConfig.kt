package com.example.rickandmortyapplication.data.paging

import androidx.paging.PagingConfig

object CharacterPagingConfig {
    const val PAGE_SIZE = 20
    const val INITIAL_LOAD_SIZE = 40
    const val PREFETCH_DISTANCE = 10

    val default: PagingConfig = PagingConfig(
        pageSize = PAGE_SIZE,
        initialLoadSize = INITIAL_LOAD_SIZE,
        prefetchDistance = PREFETCH_DISTANCE,
        enablePlaceholders = false
    )
}
