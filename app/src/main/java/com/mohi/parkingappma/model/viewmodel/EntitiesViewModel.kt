package com.mohi.parkingappma.model.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohi.parkingappma.model.domain.Entity
import com.mohi.parkingappma.model.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class EntitiesViewModel @Inject constructor(
    val getAllUseCase: GetEntitiesUseCase,
    val addUseCase: AddEntityUseCase,
    val deleteUseCase: DeleteEntityUseCase,
    val getFreeUseCase: GetFreeUseCase,
    val takeUseCase: TakeUseCase
) : ViewModel() {

    val loading = mutableStateOf(false)

    private val _listOfEntities: MutableState<List<Entity>> = mutableStateOf(emptyList())
    val listOfEntities: State<List<Entity>> = _listOfEntities

    private val _listOfTopEntities: MutableState<List<Entity>> = mutableStateOf(emptyList())
    val listOfTopEntities: State<List<Entity>> = _listOfTopEntities

    private val _listOfFreeEntities: MutableState<List<Entity>> = mutableStateOf(emptyList())
    val listOfFreeEntities: State<List<Entity>> = _listOfFreeEntities

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
            deleteUseCase(item.id)
        }
    }

    fun add(number: String, address: String, status: String, count: String) {

        viewModelScope.launch {
            try {
                val entity = Entity(
                    number = number,
                    address = address,
                    status = status,
                    count = count
                )
                Log.d("Mohi","Adding $entity")
                addUseCase(
                    entity = entity
                )
            }catch (ex: Exception) {
                Log.d("Mohi", ex.message?:"")
            }
        }
    }

    fun take(entity: Entity) {
        viewModelScope.launch {
            Log.d("Mohi","Take entity with id ${entity.id}")
            takeUseCase(entity.id)
            val list = _listOfFreeEntities.value.toMutableList()
            list.removeIf{entity.id==it.id}
            _listOfFreeEntities.value = list
        }
    }

    fun getFree() {
        viewModelScope.launch {
            val entityList = getFreeUseCase()
            _listOfFreeEntities.value = entityList
        }
    }

    fun getTop() {
        viewModelScope.launch {
            val entityList = getAllUseCase()
            _listOfTopEntities.value = entityList.sortedByDescending { it.count }.take(15)
        }
    }
}