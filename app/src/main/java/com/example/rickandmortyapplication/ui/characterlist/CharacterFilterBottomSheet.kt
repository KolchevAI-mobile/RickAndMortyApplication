package com.example.rickandmortyapplication.ui.characterlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
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
import com.example.rickandmortyapplication.ui.theme.NeonCyan
import com.example.rickandmortyapplication.ui.theme.PortalGreen
import com.example.rickandmortyapplication.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
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
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .navigationBarsPadding()
        ) {
            Text(
                text = stringResource(R.string.filter_sheet_title),
                style = MaterialTheme.typography.headlineSmall,
                color = TextSecondary
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.filters).uppercase(),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = stringResource(R.string.status_label),
                style = MaterialTheme.typography.labelLarge,
                color = TextSecondary
            )
            Spacer(Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    label = { Text(stringResource(R.string.filter_all)) },
                    selected = localFilters.status == null,
                    onClick = { localFilters = localFilters.copy(status = null) },
                    colors = chipColors(localFilters.status == null)
                )
                FilterChip(
                    label = { Text(stringResource(R.string.filter_alive)) },
                    selected = localFilters.status == CharacterStatusFilter.ALIVE,
                    onClick = { localFilters = localFilters.copy(status = CharacterStatusFilter.ALIVE) },
                    colors = chipColors(localFilters.status == CharacterStatusFilter.ALIVE)
                )
                FilterChip(
                    label = { Text(stringResource(R.string.filter_dead)) },
                    selected = localFilters.status == CharacterStatusFilter.DEAD,
                    onClick = { localFilters = localFilters.copy(status = CharacterStatusFilter.DEAD) },
                    colors = chipColors(localFilters.status == CharacterStatusFilter.DEAD)
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.gender_label),
                style = MaterialTheme.typography.labelLarge,
                color = TextSecondary
            )
            Spacer(Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    label = { Text(stringResource(R.string.filter_all)) },
                    selected = localFilters.gender == null,
                    onClick = { localFilters = localFilters.copy(gender = null) },
                    colors = chipColors(localFilters.gender == null)
                )
                FilterChip(
                    label = { Text(stringResource(R.string.filter_male)) },
                    selected = localFilters.gender == CharacterGenderFilter.MALE,
                    onClick = { localFilters = localFilters.copy(gender = CharacterGenderFilter.MALE) },
                    colors = chipColors(localFilters.gender == CharacterGenderFilter.MALE)
                )
                FilterChip(
                    label = { Text(stringResource(R.string.filter_female)) },
                    selected = localFilters.gender == CharacterGenderFilter.FEMALE,
                    onClick = { localFilters = localFilters.copy(gender = CharacterGenderFilter.FEMALE) },
                    colors = chipColors(localFilters.gender == CharacterGenderFilter.FEMALE)
                )
            }
            Spacer(Modifier.height(24.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.cancel), color = TextSecondary)
                }
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = { onApply(localFilters) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PortalGreen,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(stringResource(R.string.apply))
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun chipColors(isSelected: Boolean) = FilterChipDefaults.filterChipColors(
    containerColor = MaterialTheme.colorScheme.surfaceVariant,
    labelColor = if (isSelected) NeonCyan else TextSecondary,
    selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
    selectedLabelColor = PortalGreen
)
