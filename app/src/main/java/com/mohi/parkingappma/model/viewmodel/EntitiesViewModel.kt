package com.mohi.parkingappma.model.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohi.parkingappma.model.domain.Entity
import com.mohi.parkingappma.model.usecase.AddEntityUseCase
import com.mohi.parkingappma.model.usecase.DeleteEntityUseCase
import com.mohi.parkingappma.model.usecase.GetEntitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class EntitiesViewModel @Inject constructor(
    val getAllUseCase: GetEntitiesUseCase,
    val addUseCase: AddEntityUseCase,
    val deleteUseCase: DeleteEntityUseCase
) : ViewModel() {

    val loading = mutableStateOf(false)

    private val _listOfEntities: MutableState<List<Entity>> = mutableStateOf(emptyList())
    val listOfEntities: State<List<Entity>> = _listOfEntities

    init {
        viewModelScope.launch {
            val entityList = getAllUseCase()
            _listOfEntities.value = entityList
        }
    }

    fun onEntityClicked(it: String) {
        Log.d("Mohi","Clicked $it")
    }

    fun dismissed(item: Entity) {
        Log.d("Mohi","Deleting $item")
        viewModelScope.launch {
            val x = deleteUseCase(item.id)
        }
    }

    fun add(number: String, address: String, status: String, count: String) {
        viewModelScope.launch {
            try {
                addUseCase(
                    Entity(
                        number = number,
                        address = address,
                        status = status,
                        count = count
                    )
                )
            }catch (ex: Exception) {
                Log.d("Mohi", ex.message?:"")
            }
        }
    }
}