package com.example.paintingjournal.views.paintList

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paintingjournal.data.PaintsRepository
import com.example.paintingjournal.model.FilterSortBy
import com.example.paintingjournal.model.FilterSortByEnum
import com.example.paintingjournal.model.MiniaturePaint
import com.example.paintingjournal.model.SelectableManufacturer
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PaintListViewModel(val paintsRepository: PaintsRepository) : ViewModel() {
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    var paintListUiState by mutableStateOf(PaintListUiState())
        private set

    fun getData() {
        runBlocking {
            val paintList = paintsRepository.getAllPaintsStream()
                .filterNotNull()
                .first()
                .toList()
            paintListUiState = paintListUiState.copy(
                paintList = paintList,
                filteredPaintList = paintList
            )
        }
        runBlocking {
            createSelectableManufacturers()
        }
    }

    private suspend fun createSelectableManufacturers() {
        val manufacturers = paintsRepository.getAllManufacturers()
            .filterNotNull()
            .first()
            .toList()
        val selectableManufacturers: MutableList<SelectableManufacturer> = mutableListOf()
        manufacturers.forEach { manufacturer ->
            selectableManufacturers.add(
                SelectableManufacturer(
                    manufacturer, false
                )
            )
        }
        paintListUiState = paintListUiState.copy(selectableManufacturers = selectableManufacturers.toList())
    }

    fun togglePaintListFilter() {
        paintListUiState = paintListUiState.copy(
            showPaintListFilter = !paintListUiState.showPaintListFilter
        )
    }

    fun onManufacturerFilterCheckboxChange(manufacturer: SelectableManufacturer) {
        val selectableManufacturers: List<SelectableManufacturer> = paintListUiState.selectableManufacturers

        val selectableManufacturer = selectableManufacturers.find { it.manufacturer == manufacturer.manufacturer }
        val index = paintListUiState.selectableManufacturers.indexOf(selectableManufacturer)

        paintListUiState =
            paintListUiState.copy(selectableManufacturers = listOf())

        selectableManufacturers[index].isSelected = !selectableManufacturers[index].isSelected

        paintListUiState =
            paintListUiState.copy(selectableManufacturers = selectableManufacturers)

        applyManufacturerFilter()
    }

    private fun applyManufacturerFilter() {
        val paintList = paintListUiState.paintList
        val selectableManufacturers = paintListUiState.selectableManufacturers
        val filteredPaintList = mutableListOf<MiniaturePaint>()

        if(selectableManufacturers.any { it.isSelected }) { // Only apply filter when at least one is selected
            paintList.forEach { paint ->
                selectableManufacturers.forEach { manufacturer ->
                    if(paint.manufacturer == manufacturer.manufacturer && manufacturer.isSelected) {
                        filteredPaintList.add(paint)
                    }
                }
            }
            paintListUiState = paintListUiState.copy(filteredPaintList = filteredPaintList.toList())
        } else {
            resetFilters()
        }
    }

    private fun resetFilters() {
        paintListUiState = paintListUiState.copy(filteredPaintList = paintListUiState.paintList)
    }

    fun onChangeSorting(sortByEnum: FilterSortByEnum) {
        val filteredPaintList = paintListUiState.filteredPaintList.toMutableList()
        when(sortByEnum) {
            FilterSortByEnum.NAME_ASC -> {
                paintListUiState = paintListUiState.copy(sortBy = FilterSortBy(sortByNameAsc = true))
                filteredPaintList.sortBy { it.name }
            }
            FilterSortByEnum.NAME_DESC -> {
                paintListUiState = paintListUiState.copy(sortBy = FilterSortBy(sortByNameDesc = true))
                filteredPaintList.sortByDescending { it.name }
            }
            FilterSortByEnum.NEWEST -> {
                paintListUiState = paintListUiState.copy(sortBy = FilterSortBy(sortByNewest = true))
                filteredPaintList.sortByDescending { it.createdAt }
            }
            FilterSortByEnum.OLDEST -> {
                paintListUiState = paintListUiState.copy(sortBy = FilterSortBy(sortByOldest = true))
                filteredPaintList.sortBy { it.createdAt }
            }

            FilterSortByEnum.COLOR_ASC -> {
                paintListUiState = paintListUiState.copy(sortBy = FilterSortBy(sortByColorAsc = true))
                filteredPaintList.sortBy { it.hexColor }
            }
            FilterSortByEnum.COLOR_DESC -> {
                paintListUiState = paintListUiState.copy(sortBy = FilterSortBy(sortByColorDesc = true))
                filteredPaintList.sortByDescending { it.hexColor }
            }
        }
        paintListUiState = paintListUiState.copy(filteredPaintList = listOf())
        paintListUiState = paintListUiState.copy(filteredPaintList = filteredPaintList.toList())
    }

    fun onSearchBarValueChanged(value: String) {
        paintListUiState = paintListUiState.copy(searchBarValue = value)
    }
}

data class PaintListUiState(
    val paintList: List<MiniaturePaint> = listOf(),
    val filteredPaintList: List<MiniaturePaint> = listOf(),
    val paintManufacturers: List<String> = listOf(),
    val showPaintListFilter: Boolean = false,
    val selectableManufacturers: List<SelectableManufacturer> = listOf(),
    val sortBy: FilterSortBy = FilterSortBy(),
    val searchBarValue: String = "",
)