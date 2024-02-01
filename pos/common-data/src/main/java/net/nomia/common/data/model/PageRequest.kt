package net.nomia.common.data.model

data class PageRequest(
    val page: Int,
    val size: Long,
    val sort: List<SortOrder> = emptyList()
) {
    data class SortOrder(
        val fieldName: String,
        val direction: SortDirection
    )

    enum class SortDirection {
        Asc,
        Desc,
    }
}
