package com.example.paintingjournal.model

data class FilterSortBy (
    val sortByNameAsc: Boolean = false,
    val sortByNameDesc: Boolean = false,
    val sortByNewest: Boolean = false,
    val sortByOldest: Boolean = false
)