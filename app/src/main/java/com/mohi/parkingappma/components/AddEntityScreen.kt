package com.mohi.parkingappma.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mohi.parkingappma.model.viewmodel.EntitiesViewModel

@Composable
fun AddEntityScreen(
    viewModel: EntitiesViewModel = hiltViewModel(),
    onClick: (String, String, String, String) -> Unit
) {
    var textNumber by rememberSaveable { mutableStateOf("") }
    var textAddress by rememberSaveable { mutableStateOf("") }
    var textStatus by rememberSaveable { mutableStateOf("") }
    var textCount by rememberSaveable { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(0.9f)) {
        Column() {
            TextField(
                value = textNumber,
                onValueChange = {
                    textNumber = it
                },
                label = { Text("Number") }
            )
            TextField(
                value = textAddress,
                onValueChange = {
                    textAddress = it
                },
                label = { Text("Address") }
            )
            TextField(
                value = textStatus,
                onValueChange = {
                    textStatus = it
                },
                label = { Text("Status") }
            )
            TextField(
                value = textCount,
                onValueChange = {
                    textCount = it
                },
                label = { Text("Count") }
            )
        }
        FloatingActionButton(
            modifier = Modifier.align(Alignment.BottomEnd).padding(25.dp),
            onClick = { onClick(textNumber, textAddress, textStatus, textCount) }
        ) {
            Text(text = "Save")
        }
    }
}