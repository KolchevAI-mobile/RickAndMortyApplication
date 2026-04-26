package com.example.rickandmortyapplication.domain.error

sealed class GetCharacterError {
    data object NotFound : GetCharacterError()
    data class Network(val cause: Throwable) : GetCharacterError()
    data class Unknown(val cause: Throwable) : GetCharacterError()
}
