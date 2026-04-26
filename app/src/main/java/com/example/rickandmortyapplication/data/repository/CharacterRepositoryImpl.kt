package com.example.rickandmortyapplication.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.rickandmortyapplication.data.local.AppDatabase
import com.example.rickandmortyapplication.data.mapper.toApiQuery
import com.example.rickandmortyapplication.data.mapper.toCharacter
import com.example.rickandmortyapplication.data.mapper.toCharacterEntity
import com.example.rickandmortyapplication.data.paging.NetworkCharacterPagingSource
import com.example.rickandmortyapplication.data.remote.CharacterRemoteMediator
import com.example.rickandmortyapplication.data.remote.RickAndMortyApi
import com.example.rickandmortyapplication.domain.GetCharacterByIdResult
import com.example.rickandmortyapplication.domain.error.GetCharacterError
import com.example.rickandmortyapplication.domain.model.Character
import com.example.rickandmortyapplication.domain.model.CharacterFilter
import com.example.rickandmortyapplication.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val api: RickAndMortyApi,
    private val database: AppDatabase
) : CharacterRepository, CharacterPagingDataSource {

    @OptIn(ExperimentalPagingApi::class)
    override fun observeCachedLocalWithRemote(): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = CharacterRemoteMediator(database, api),
            pagingSourceFactory = { database.characterDao().getPagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { it.toCharacter() }
        }
    }

    override fun observeNetworkOnly(
        query: String,
        filters: CharacterFilter
    ): Flow<PagingData<Character>> {
        val apiFilter = filters.toApiQuery()
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                NetworkCharacterPagingSource(api, query, apiFilter)
            }
        ).flow
    }

    override suspend fun getCharacterById(id: Int): GetCharacterByIdResult {
        database.characterDao().getById(id)?.toCharacter()?.let { cached ->
            return GetCharacterByIdResult.Success(cached)
        }
        return try {
            val dto = api.getCharacterById(id)
            GetCharacterByIdResult.Success(dto.toCharacterEntity().toCharacter())
        } catch (e: CancellationException) {
            throw e
        } catch (e: HttpException) {
            when (e.code()) {
                404 -> GetCharacterByIdResult.Failure(GetCharacterError.NotFound)
                else -> GetCharacterByIdResult.Failure(GetCharacterError.Unknown(e))
            }
        } catch (e: IOException) {
            GetCharacterByIdResult.Failure(GetCharacterError.Network(e))
        } catch (e: Exception) {
            GetCharacterByIdResult.Failure(GetCharacterError.Unknown(e))
        }
    }
}
