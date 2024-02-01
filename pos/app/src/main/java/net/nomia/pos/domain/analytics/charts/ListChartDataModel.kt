package net.nomia.pos.domain.analytics.charts

data class ListChartDataModel(
    val number: Int,
    val name: String,
    val data: List<Int>
)