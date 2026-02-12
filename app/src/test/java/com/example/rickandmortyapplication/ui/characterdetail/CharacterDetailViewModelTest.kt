package com.example.rickandmortyapplication.ui.characterdetail

import androidx.lifecycle.SavedStateHandle
import com.example.rickandmortyapplication.domain.model.Character
import com.example.rickandmortyapplication.domain.repository.CharacterRepository
import com.example.rickandmortyapplication.domain.use_case.GetCharacterByIdUseCase
import com.example.rickandmortyapplication.ui.state.UiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterDetailViewModelTest {

    private class FakeSuccessRepository : CharacterRepository {
        override fun getAllCharacters() =
            throw UnsupportedOperationException()

        override suspend fun getCharacterById(id: Int): Character {
            return Character(
                id = id,
                name = "Rick Sanchez",
                status = "Alive",
                species = "Human",
                image = "https://example.com/rick.png"
            )
        }

        override fun getCharacters(
            query: String,
            filters: com.example.rickandmortyapplication.domain.model.CharacterFilter
        ) = throw UnsupportedOperationException()
    }

    private class FakeErrorRepository : CharacterRepository {
        override fun getAllCharacters() =
            throw UnsupportedOperationException()

        override suspend fun getCharacterById(id: Int): Character {
            throw RuntimeException("Network error")
        }

        override fun getCharacters(
            query: String,
            filters: com.example.rickandmortyapplication.domain.model.CharacterFilter
        ) = throw UnsupportedOperationException()
    }

    @Test
    fun `when repository returns character, state is Success`() = runTest {
        val repo = FakeSuccessRepository()
        val useCase = GetCharacterByIdUseCase(repo)
        val savedStateHandle = SavedStateHandle(mapOf("id" to 1))
        val viewModel = CharacterDetailViewModel(
            savedStateHandle = savedStateHandle,
            getCharacterById = useCase
        )

        viewModel.loadCharacter()

        val state = viewModel.state.value
        require(state is UiState.Success)
        assertEquals(1, state.data.id)
        assertEquals("Rick Sanchez", state.data.name)
    }

    @Test
    fun `when repository throws, state is Error`() = runTest {
        val repo = FakeErrorRepository()
        val useCase = GetCharacterByIdUseCase(repo)
        val savedStateHandle = SavedStateHandle(mapOf("id" to 1))
        val viewModel = CharacterDetailViewModel(
            savedStateHandle = savedStateHandle,
            getCharacterById = useCase
        )

        viewModel.loadCharacter()

        val state = viewModel.state.value
        require(state is UiState.Error)
        assertEquals("Network error", state.message)
    }
}
