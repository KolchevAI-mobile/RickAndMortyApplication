package com.example.rickandmortyapplication.ui.characterlist

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.rickandmortyapplication.R
import java.io.IOException
import retrofit2.HttpException

@Composable
fun pagingErrorMessage(throwable: Throwable): String = when (throwable) {
    is IOException -> stringResource(R.string.error_network)
    is HttpException -> when (throwable.code()) {
        in 500..599 -> stringResource(R.string.error_server)
        else -> stringResource(R.string.load_characters_error)
    }
    else -> throwable.localizedMessage ?: stringResource(R.string.load_characters_error)
}
