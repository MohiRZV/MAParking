package com.mohi.parkingappma.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mohi.parkingappma.model.domain.Entity
import com.mohi.parkingappma.model.viewmodel.EntitiesViewModel

@ExperimentalMaterialApi
@Composable
fun DisplayAvailableScreen(
    viewModel: EntitiesViewModel = hiltViewModel(),
    onTakeClick : (Entity) -> Unit
) {
    val loading by remember { viewModel.loading }
    viewModel.getFree()
    val listOfEntities by remember { viewModel.listOfFreeEntities }

    Box(modifier = Modifier) {
        LazyColumn {
            items(listOfEntities) { item ->
                val rememberState = remember { mutableStateOf(true) }
                if(rememberState.value)
                    SingleAvailableEntityItem(
                        entity = item,
                        onClick = onTakeClick,
                        state = rememberState
                    )
            }
        }
        CircularIndeterminateProgressBar(isDisplayed = loading)
    }
}

@ExperimentalMaterialApi
@Composable
fun SingleAvailableEntityItem(
    entity: Entity,
    onClick: (Entity) -> Unit,
    state: MutableState<Boolean>
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = entity.number
            )
            Button(modifier = Modifier.padding(start = 150.dp), onClick = {
                onClick(entity)
                state.value = false
            }) {
                Box(modifier = Modifier) {
                    Text(text = "Take")
                }
            }
        }
    }
}