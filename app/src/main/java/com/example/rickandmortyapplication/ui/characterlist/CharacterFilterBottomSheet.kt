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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rickandmortyapplication.domain.model.CharacterFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterFilterBottomSheet(
    currentFilters: CharacterFilter,
    onApply: (CharacterFilter) -> Unit,
    onDismiss: () -> Unit
) {
    var localFilters by remember { mutableStateOf(currentFilters) }

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Статус")
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterOptionButton(
                    label = "Все",
                    selected = localFilters.status == null,
                    onClick = { localFilters = localFilters.copy(status = null) }
                )
                FilterOptionButton(
                    label = "Живой",
                    selected = localFilters.status == "alive",
                    onClick = { localFilters = localFilters.copy(status = "alive") }
                )
                FilterOptionButton(
                    label = "Мертвый",
                    selected = localFilters.status == "dead",
                    onClick = { localFilters = localFilters.copy(status = "dead") }
                )
            }

            Text(text = "Пол")
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterOptionButton(
                    label = "Все",
                    selected = localFilters.gender == null,
                    onClick = { localFilters = localFilters.copy(gender = null) }
                )
                FilterOptionButton(
                    label = "Мужской",
                    selected = localFilters.gender == "male",
                    onClick = { localFilters = localFilters.copy(gender = "male") }
                )
                FilterOptionButton(
                    label = "Женский",
                    selected = localFilters.gender == "female",
                    onClick = { localFilters = localFilters.copy(gender = "female") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text("Отмена")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { onApply(localFilters) }) {
                    Text("Применить")
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
