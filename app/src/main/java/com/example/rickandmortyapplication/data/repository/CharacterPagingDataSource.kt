package com.example.rickandmortyapplication.data.repository

import androidx.paging.PagingData
import com.example.rickandmortyapplication.domain.model.Character
import com.example.rickandmortyapplication.domain.model.CharacterFilter
import kotlinx.coroutines.flow.Flow

/**
 * Пагинация остаётся в data-слое (AndroidX Paging), не в domain-репозитории.
 */
interface CharacterPagingDataSource {

    fun observeCachedLocalWithRemote(): Flow<PagingData<Character>>

    fun observeNetworkOnly(
        query: String,
        filters: CharacterFilter
    ): Flow<PagingData<Character>>
}
