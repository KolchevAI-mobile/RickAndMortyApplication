package com.example.rickandmortyapplication.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {

    @Serializable
    data object CharacterList : Screen

    @Serializable
    data class CharacterDetail(val id: Int) : Screen
}
