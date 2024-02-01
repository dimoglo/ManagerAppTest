package net.nomia.common.data.model

data class Page<T>(
    val content: List<T>,
    val pageNumber: Int,
    val pageSize: Int,
    val total: Int,
    val totalPages: Int
)
