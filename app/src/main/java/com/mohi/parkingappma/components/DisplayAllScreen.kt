package com.mohi.parkingappma.components

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mohi.parkingappma.model.domain.Entity
import com.mohi.parkingappma.model.viewmodel.EntitiesViewModel
import com.mohi.parkingappma.utils.InternetStatus
import com.mohi.parkingappma.utils.InternetStatusLive

@ExperimentalMaterialApi
@Composable
fun DisplayAllScreen(
    viewModel: EntitiesViewModel = hiltViewModel(),
    onClick: () -> Unit,
    onSwitchToUserView: () -> Unit,
    onSwitchToStatsView: () -> Unit
) {
    val loading by remember { viewModel.loading }
    val listOfEntities by remember { viewModel.listOfEntities }

    Box(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()) {
        LazyColumn {
            items(listOfEntities) { item ->
                val dismissState = rememberDismissState()
                if (dismissState.isDismissed(DismissDirection.StartToEnd)) {
                    viewModel.dismissed(item)
                } else {
                    SwipeToDismiss(
                        state = dismissState,
                        modifier = Modifier.padding(vertical = 1.dp),
                        directions = setOf(DismissDirection.StartToEnd),
                        dismissThresholds = { direction ->
                            FractionalThreshold(if (direction == DismissDirection.StartToEnd) 0.25f else 0.5f)
                        },
                        background = {
                            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
                            val color by animateColorAsState(
                                when (dismissState.targetValue) {
                                    DismissValue.Default -> Color.LightGray
                                    DismissValue.DismissedToEnd -> Color.Red
                                    else -> Color.Green
                                }
                            )
                            val alignment = when (direction) {
                                DismissDirection.StartToEnd -> Alignment.CenterStart
                                DismissDirection.EndToStart -> Alignment.CenterEnd
                            }
                            val icon = when (direction) {
                                DismissDirection.StartToEnd -> Icons.Default.Delete
                                DismissDirection.EndToStart -> Icons.Default.Close
                            }
                            val scale by animateFloatAsState(
                                if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                            )

                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .background(color)
                                    .padding(horizontal = 20.dp),
                                contentAlignment = alignment
                            ) {
                                Icon(
                                    icon,
                                    contentDescription = "Localized description",
                                    modifier = Modifier.scale(scale)
                                )
                            }
                        },
                        dismissContent = {
                            SingleEntityItem(
                                entity = item,
                                dismissState = dismissState
                            )
                            Card(
                                elevation = animateDpAsState(
                                    if (dismissState.dismissDirection != null) 4.dp else 0.dp
                                ).value
                            ) {
                                Text(item.number)
                            }
                        }
                    )
                }
            }

        }
        CircularIndeterminateProgressBar(isDisplayed = loading)
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(25.dp),
            onClick = { onClick() }
        ) {
            Text(text = "Add")
        }
        val context = LocalContext.current
        Button(
            onClick = {

                if (InternetStatusLive.status.value == InternetStatus.ONLINE)
                    onSwitchToUserView()
                else
                    Toast.makeText(context, "Only available online!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(end = 25.dp, bottom = 25.dp)
        ) {
            Text(text = "UserView")
        }

        Button(
            onClick = {
                if (InternetStatusLive.status.value == InternetStatus.ONLINE)
                    onSwitchToStatsView()
                else
                    Toast.makeText(context, "Only available online!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 25.dp, end = 25.dp, bottom = 25.dp)
        ) {
            Text(text = "StatsView")
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun SingleEntityItem(
    entity: Entity,
    dismissState: DismissState
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                Text(text = "number: ${entity.number}")
                Spacer(modifier = Modifier.padding(start = 25.dp, end = 25.dp))
                Text(text = "count: ${entity.count}")
            }
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                Text(text = "address: ${entity.address}")
                Spacer(modifier = Modifier.padding(start = 25.dp, end = 25.dp))
                Text(text = "status: ${entity.status}")
            }
        }
    }
}