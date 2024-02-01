package net.nomia.pos.ui.analytics

import net.nomia.pos.domain.analytics.AnalyticsWidgetModel

data class AnalyticsState(
    val isLoading: Boolean = false,
    val data: List<AnalyticsWidgetModel> = listOf(),
    val error: String? = null,
)