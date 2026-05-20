package com.example.rickandmortyapplication.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.rickandmortyapplication.data.local.AppDatabase
import com.example.rickandmortyapplication.data.local.entity.CharacterEntity
import com.example.rickandmortyapplication.data.local.entity.RemoteKeysEntity
import com.example.rickandmortyapplication.data.mapper.toCharacterEntity
import retrofit2.HttpException
import java.io.IOException
@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val database: AppDatabase,
    private val api: RickAndMortyApi
) : RemoteMediator<Int, CharacterEntity>() {

    override suspend fun initialize(): InitializeAction {
        return if (database.characterDao().count() > 0) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> FIRST_PAGE
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    nextKey
                }
            }

            val response = api.loadCharacterPage(page = page)
            val endOfPaginationReached = response.info.next == null

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().clearRemoteKeys()
                    database.characterDao().clearAll()
                }

                val (prevKey, nextKey) = pageKeys(page, hasNextPage = !endOfPaginationReached)

                val remoteKeys = response.results.map { dto ->
                    RemoteKeysEntity(
                        id = dto.id,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                database.remoteKeysDao().insertAll(remoteKeys)
                database.characterDao().insertAll(response.results.map { it.toCharacterEntity() })
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, CharacterEntity>): RemoteKeysEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { character -> database.remoteKeysDao().getRemoteKeys(character.id) }
    }

    private companion object {
        const val FIRST_PAGE = 1
    }
}
