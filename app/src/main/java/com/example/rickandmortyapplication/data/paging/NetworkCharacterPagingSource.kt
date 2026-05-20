package com.example.rickandmortyapplication.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickandmortyapplication.data.mapper.ApiCharacterFilter
import com.example.rickandmortyapplication.data.mapper.toCharacter
import com.example.rickandmortyapplication.data.mapper.toCharacterEntity
import com.example.rickandmortyapplication.data.remote.RickAndMortyApi
import com.example.rickandmortyapplication.data.remote.loadCharacterPage
import com.example.rickandmortyapplication.data.remote.pageKeys
import com.example.rickandmortyapplication.domain.model.Character
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class NetworkCharacterPagingSource(
    private val api: RickAndMortyApi,
    private val query: String,
    private val filter: ApiCharacterFilter
) : PagingSource<Int, Character>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        return try {
            val page = params.key ?: 1
            val response = api.loadCharacterPage(
                page = page,
                name = query.ifBlank { null },
                status = filter.status,
                gender = filter.gender
            )
            val characters = response.results.map { dto ->
                dto.toCharacterEntity().toCharacter()
            }
            val endOfPagination = response.info.next == null
            val (prevKey, nextKey) = pageKeys(page, hasNextPage = !endOfPagination)
            LoadResult.Page(
                data = characters,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            PagingLoadResultFactory.fromThrowable(e)
        } catch (e: HttpException) {
            PagingLoadResultFactory.fromThrowable(e)
        } catch (e: Throwable) {
            PagingLoadResultFactory.fromThrowable(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
