package com.mohi.parkingappma.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mohi.parkingappma.model.domain.Entity
import com.mohi.parkingappma.model.viewmodel.EntitiesViewModel

@ExperimentalMaterialApi
@Composable
fun DisplayStatsScreen(
    viewModel: EntitiesViewModel = hiltViewModel()
) {
    val loading by remember { viewModel.loading }
    viewModel.getTop()
    val listOfEntities by remember { viewModel.listOfTopEntities }

    Box(modifier = Modifier) {
        LazyColumn {
            items(listOfEntities) { item ->
                SingleStatsEntityItem(
                    entity = item
                )
            }

        }
        CircularIndeterminateProgressBar(isDisplayed = loading)
    }
}

@ExperimentalMaterialApi
@Composable
fun SingleStatsEntityItem(
    entity: Entity
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = entity.number
            )
        }
    }
}