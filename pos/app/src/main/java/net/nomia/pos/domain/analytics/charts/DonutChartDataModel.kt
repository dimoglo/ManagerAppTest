package net.nomia.pos.domain.analytics.charts


data class DonutChartDataModel(
    val icon: Int?,
    val name: String,
    val data: List<Int>,
    val color: Int
)