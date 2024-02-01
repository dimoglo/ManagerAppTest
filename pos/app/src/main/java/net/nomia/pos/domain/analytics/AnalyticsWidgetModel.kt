package net.nomia.pos.domain.analytics

import net.nomia.erp.schema.type.PaySystemIcon

data class AnalyticsWidgetModel(
    val title: String,
    val dateRange: String,
    val tabs: List<TabModel>,
    val chartType: ChartType,
    val chartData: List<Any>
)

data class TabModel(
    val type: TabType,
    val data: String? = null
)

enum class ChartType {
    LINE,
    BAR,
    DONUT,
    LIST
}

enum class TabType(
    val displayName: String,
    val hasSuffix: Boolean = false
) {
    REVENUE("Выручка", true),
    PROFIT("Прибыль", true),
    ORDER_COUNT("Заказы"),
    GUEST_COUNT("Гости"),
    AVERAGE_CHECK("Средний чек", true),
    SALES("Продажи")
}