package com.example.rickandmortyapplication.ui.characterlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.rickandmortyapplication.R
import com.example.rickandmortyapplication.domain.model.CharacterFilter
import com.example.rickandmortyapplication.domain.model.CharacterGenderFilter
import com.example.rickandmortyapplication.domain.model.CharacterStatusFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterFilterBottomSheet(
    currentFilters: CharacterFilter,
    onApply: (CharacterFilter) -> Unit,
    onDismiss: () -> Unit
) {
    var localFilters by remember { mutableStateOf(currentFilters) }

    LaunchedEffect(currentFilters) {
        localFilters = currentFilters
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = stringResource(R.string.status_label))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterOptionButton(
                    label = stringResource(R.string.filter_all),
                    selected = localFilters.status == null,
                    onClick = { localFilters = localFilters.copy(status = null) }
                )
                FilterOptionButton(
                    label = stringResource(R.string.filter_alive),
                    selected = localFilters.status == CharacterStatusFilter.ALIVE,
                    onClick = { localFilters = localFilters.copy(status = CharacterStatusFilter.ALIVE) }
                )
                FilterOptionButton(
                    label = stringResource(R.string.filter_dead),
                    selected = localFilters.status == CharacterStatusFilter.DEAD,
                    onClick = { localFilters = localFilters.copy(status = CharacterStatusFilter.DEAD) }
                )
            }

            Text(text = stringResource(R.string.gender_label))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterOptionButton(
                    label = stringResource(R.string.filter_all),
                    selected = localFilters.gender == null,
                    onClick = { localFilters = localFilters.copy(gender = null) }
                )
                FilterOptionButton(
                    label = stringResource(R.string.filter_male),
                    selected = localFilters.gender == CharacterGenderFilter.MALE,
                    onClick = { localFilters = localFilters.copy(gender = CharacterGenderFilter.MALE) }
                )
                FilterOptionButton(
                    label = stringResource(R.string.filter_female),
                    selected = localFilters.gender == CharacterGenderFilter.FEMALE,
                    onClick = { localFilters = localFilters.copy(gender = CharacterGenderFilter.FEMALE) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.cancel))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { onApply(localFilters) }) {
                    Text(stringResource(R.string.apply))
                }
            }
        }
    }
}

@Composable
private fun FilterOptionButton(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    TextButton(onClick = onClick) {
        Text(
            text = if (selected) "• $label" else label
        )
    }
}
