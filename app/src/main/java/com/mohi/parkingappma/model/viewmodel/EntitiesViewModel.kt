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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class EntitiesViewModel @Inject constructor(
    val getAllUseCase: GetEntitiesUseCase,
    val addUseCase: AddEntityUseCase,
    val deleteUseCase: DeleteEntityUseCase,
    val getFreeUseCase: GetFreeUseCase,
    val takeUseCase: TakeUseCase,
    val syncUseCase: SyncUseCase
) : ViewModel() {

    val loading = mutableStateOf(false)

    private val _listOfEntities: MutableState<List<Entity>> = mutableStateOf(emptyList())
    val listOfEntities: State<List<Entity>> = _listOfEntities

    private val _listOfTopEntities: MutableState<List<Entity>> = mutableStateOf(emptyList())
    val listOfTopEntities: State<List<Entity>> get() {
        getTop()
        return _listOfTopEntities
    }

    private val _listOfFreeEntities: MutableState<List<Entity>> = mutableStateOf(emptyList())
    val listOfFreeEntities: State<List<Entity>> get() {
       getFree()
       return _listOfFreeEntities
    }

    init {
        viewModelScope.launch {
            loading.value = true
            val entityList = getAllUseCase()
            _listOfEntities.value = entityList
            delay(1000)
            loading.value = false
        }
    }

    fun dismissed(item: Entity) {
        Log.d("Mohi","Deleting $item")
        viewModelScope.launch {
            try {
                loading.value = true
                deleteUseCase(item)
                loading.value = false
            } catch (ex: HttpException) {
                Log.d("Mohi",ex.message())
            }
        }
    }

    fun add(number: String, address: String, status: String, count: String) {
        viewModelScope.launch {
            loading.value = true
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
                loading.value = false
            }catch (ex: Exception) {
                Log.d("Mohi", ex.message?:"")
            }
        }
    }

    fun take(entity: Entity) {
        loading.value = true
        viewModelScope.launch {
            Log.d("Mohi","Take entity with id ${entity.id}")
            takeUseCase(entity.id)
            val list = _listOfFreeEntities.value.toMutableList()
            list.removeIf{entity.id==it.id}
            _listOfFreeEntities.value = list
            loading.value = false
        }
    }

    private fun getFree() {
        viewModelScope.launch {
            loading.value = true
            Log.d("Mohi","Obtaining available places")
            val entityList = getFreeUseCase()
            delay(1000)
            _listOfFreeEntities.value = entityList
            loading.value = false
        }
    }

    private fun getTop() {
        viewModelScope.launch {
            loading.value = true
            Log.d("Mohi","Generating top most used spaces")
            val entityList = getAllUseCase()
            delay(1000)
            _listOfTopEntities.value = entityList.sortedByDescending { it.count }.take(15)
            loading.value = false
        }
    }

    fun backOnline() {

        viewModelScope.launch {
            loading.value = true
            Log.d("Mohi","Synchronizing server with local data")
            try {
                syncUseCase()
                delay(1000)
                loading.value = false
            } catch (ex: Exception) {
                Log.d("Mohi", "Something went wrong while sync with server")
            }

        }
    }
}