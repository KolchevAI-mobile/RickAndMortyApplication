package com.example.rickandmortyapplication.ui.characterdetail

import androidx.lifecycle.SavedStateHandle
import com.example.rickandmortyapplication.domain.GetCharacterByIdResult
import com.example.rickandmortyapplication.domain.error.GetCharacterError
import com.example.rickandmortyapplication.domain.model.Character
import com.example.rickandmortyapplication.domain.repository.CharacterRepository
import com.example.rickandmortyapplication.domain.use_case.GetCharacterByIdUseCase
import com.example.rickandmortyapplication.test.MainDispatcherRule
import com.example.rickandmortyapplication.ui.state.UiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.DEFAULT_MANIFEST_NAME, minSdk = 28)
class CharacterDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private class FakeSuccessRepository : CharacterRepository {
        override suspend fun getCharacterById(id: Int): GetCharacterByIdResult {
            return GetCharacterByIdResult.Success(
                Character(
                    id = id,
                    name = "Rick Sanchez",
                    status = "Alive",
                    species = "Human",
                    image = "https://example.com/rick.png"
                )
            )
        }
    }

    private class FakeErrorRepository : CharacterRepository {
        override suspend fun getCharacterById(id: Int): GetCharacterByIdResult {
            return GetCharacterByIdResult.Failure(
                GetCharacterError.Unknown(RuntimeException("Network error"))
            )
        }
    }

    @Test
    fun `when repository returns character, state is Success`() = runTest {
        val repo = FakeSuccessRepository()
        val useCase = GetCharacterByIdUseCase(repo)
        val savedStateHandle = SavedStateHandle(
            mapOf("id" to 1)
        )
        val viewModel = CharacterDetailViewModel(
            savedStateHandle = savedStateHandle,
            getCharacterById = useCase
        )
        val state = viewModel.state.value
        require(state is UiState.Success)
        assertEquals(1, state.data.id)
        assertEquals("Rick Sanchez", state.data.name)
    }

    @Test
    fun `when repository returns failure, state is Error`() = runTest {
        val repo = FakeErrorRepository()
        val useCase = GetCharacterByIdUseCase(repo)
        val viewModel = CharacterDetailViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf("id" to 1)
            ),
            getCharacterById = useCase
        )
        val state = viewModel.state.value
        require(state is UiState.Error)
        val reason = state.error
        require(reason is GetCharacterError.Unknown)
        assertEquals("Network error", reason.cause.message)
    }
}
