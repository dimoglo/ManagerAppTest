package net.nomia.pos.domain.analytics

interface AnalyticsRepository {
    suspend fun getAnalytics(): List<AnalyticsWidgetModel>
}