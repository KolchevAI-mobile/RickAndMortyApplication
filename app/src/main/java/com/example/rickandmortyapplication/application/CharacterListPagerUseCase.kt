package com.example.rickandmortyapplication.application

import androidx.paging.PagingData
import com.example.rickandmortyapplication.data.repository.CharacterPagingDataSource
import com.example.rickandmortyapplication.domain.model.Character
import com.example.rickandmortyapplication.domain.model.CharacterFilter
import com.example.rickandmortyapplication.domain.policy.CharacterListBrowsePolicy
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case уровня приложения: связывает domain-политику просмотра списка с
 * data-источником пагинации (AndroidX Paging остаётся вне domain).
 */
class CharacterListPagerUseCase @Inject constructor(
    private val pagingDataSource: CharacterPagingDataSource
) {
    operator fun invoke(
        query: String,
        filters: CharacterFilter
    ): Flow<PagingData<Character>> {
        return if (
            CharacterListBrowsePolicy.shouldUseLocalCacheWithRemoteMediator(
                query,
                filters
            )
        ) {
            pagingDataSource.observeCachedLocalWithRemote()
        } else {
            pagingDataSource.observeNetworkOnly(query, filters)
        }
    }
}
