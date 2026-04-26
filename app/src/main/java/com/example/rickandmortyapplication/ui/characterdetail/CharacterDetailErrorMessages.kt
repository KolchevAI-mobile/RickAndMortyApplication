package com.example.rickandmortyapplication.ui.characterdetail

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.rickandmortyapplication.R
import com.example.rickandmortyapplication.domain.error.GetCharacterError
import java.io.IOException

@Composable
fun getCharacterErrorString(error: GetCharacterError): String = when (error) {
    is GetCharacterError.NotFound -> stringResource(R.string.error_character_not_found)
    is GetCharacterError.Network -> stringResource(R.string.error_network)
    is GetCharacterError.Unknown -> {
        if (error.cause is IOException) {
            stringResource(R.string.error_network)
        } else {
            stringResource(R.string.load_character_error)
        }
    }
}
