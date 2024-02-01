package net.nomia.pos.data

import android.util.Log
import com.apollographql.apollo.api.Input
import kotlinx.coroutines.flow.first
import net.nomia.erp.api.ErpClientService
import net.nomia.erp.apiFlow
import net.nomia.erp.query.GetAllPaySystemsByStoreIdQuery
import net.nomia.erp.query.GetMostPopularCategoriesQuery
import net.nomia.erp.query.GetMostPopularProductsQuery
import net.nomia.erp.query.GetPaySystemReportV2Query
import net.nomia.erp.query.GetSalesByWeekDayQuery
import net.nomia.erp.schema.type.DateRangeInput
import net.nomia.erp.schema.type.MostPopularCategoriesWidgetInput
import net.nomia.erp.schema.type.MostPopularProductsWidgetInput
import net.nomia.erp.schema.type.PaySystemIcon
import net.nomia.erp.schema.type.PaySystemReportInput
import net.nomia.erp.schema.type.SalesByWeekDayWidgetInput
import net.nomia.pos.R
import net.nomia.pos.domain.analytics.AnalyticsRepository
import net.nomia.pos.domain.analytics.AnalyticsWidgetModel
import net.nomia.pos.domain.analytics.ChartType
import net.nomia.pos.domain.analytics.TabModel
import net.nomia.pos.domain.analytics.TabType
import net.nomia.pos.domain.analytics.charts.BarChartDataModel
import net.nomia.pos.domain.analytics.charts.DonutChartDataModel
import net.nomia.pos.domain.analytics.charts.ListChartDataModel
import net.nomia.pos.domain.auth.pos_setup.use_cases.AuthUseCase
import net.nomia.settings.domain.SettingsRepository
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

class AnalyticsRepositoryImpl @Inject constructor(
    val repository: SettingsRepository,
    val erpClientService: ErpClientService,
    val authUseCase: AuthUseCase,
): AnalyticsRepository {

    companion object {
        const val ANALYTICS_TITLE_TODAY = "Сегодня"
        const val ANALYTICS_TITLE_SALES = "Продажи"
        const val ANALYTICS_TITLE_SALES_BY_WEEK_DAY = "Продажи по дням недели"
        const val ANALYTICS_TITLE_SALES_BY_TIME = "Продажи по времени"
        const val ANALYTICS_TITLE_PAY_SYSTEM = "Способы оплаты"
        const val ANALYTICS_TITLE_PLACE = "Способ заказа"
        const val ANALYTICS_TITLE_PRODUCTS = "Популярные продукты"
        const val ANALYTICS_TITLE_CATEGORY = "Популярные категории"

        val DonutColors = arrayOf(R.color.donut_1, R.color.donut_2, R.color.donut_3)

        const val EMPTY_PAY_SYSTEMS_RESPONSE = "Не удалось получить список платежных систем"
        const val EMPTY_PAY_SYSTEMS_REPORT_RESPONSE = "Не удалось получить аналитику платежных систем"
        const val EMPTY_POPULAR_PRODUCTS_REPORT_RESPONSE = "Не удалось получить аналитику по популярным товарам"
        const val EMPTY_POPULAR_CATEGORIES_REPORT_RESPONSE = "Не удалось получить аналитику по популярным категориям"
        const val EMPTY_SALES_BY_WEEK_DAY_REPORT_RESPONSE = "Не удалось получить аналитику по популярным категориям"
    }

    override suspend fun getAnalytics(): List<AnalyticsWidgetModel> {
        return listOf(getSalesByWeekDay(), getPaySystemReport(), getPopularProduct(), getPopularCategories())
    }

    private suspend fun getPaySystemReport(): AnalyticsWidgetModel {
        val dateFrom: LocalDate = LocalDate.of(2023, 10, 1)
        val dateTo: LocalDate = LocalDate.now()
        val terminal = repository.getTerminal().first()

        val auth = authUseCase.get() ?: throw Exception()

        val client = erpClientService.principalClient(
            token = auth.accessToken,
            organizationId = terminal!!.organization.id
        ).first()

        val paySystemsQuery = GetAllPaySystemsByStoreIdQuery(terminal.storeId.value)

        val paySystemsResponse = client.query(paySystemsQuery)
            .apiFlow()
            .first()
            .data
            ?.allPaySystemsByStoreId ?: throw Exception(EMPTY_PAY_SYSTEMS_RESPONSE)

        val paySystemIds = paySystemsResponse.map { it.fragments.paySystemFragment.id }

        val paySystemIdsInput: Input<List<UUID>> = Input.fromNullable(paySystemIds)

        val response = client.query(
            GetPaySystemReportV2Query(
                input = PaySystemReportInput(
                    dateRange = DateRangeInput(dateFrom, dateTo),
                    paySystemIds = paySystemIdsInput,
                    storeIds = Input.fromNullable(listOf(terminal.storeId.value))
                )
            )
        )
            .apiFlow()
            .first().data

        val parsedData = response?.paySystemReportV2?.rows?.content?.mapIndexed { index, paySystemReport ->
            DonutChartDataModel(
                icon = paySystemsResponse.getIconResourceById(paySystemReport.paySystemId),
                name = paySystemReport.paySystemName,
                data = listOf(
                    paySystemReport.revenue.toInt(),
                    paySystemReport.ordersQuantity,
                    paySystemReport.guestsCount,
                    paySystemReport.averageCheck.toInt(),
                ),
                color = DonutColors[index]
            )
        } ?: throw Exception(EMPTY_PAY_SYSTEMS_REPORT_RESPONSE)

        return AnalyticsWidgetModel(
            title = ANALYTICS_TITLE_PAY_SYSTEM,
            dateRange = "$dateFrom - $dateTo",
            tabs = listOf(
                TabModel(type = TabType.REVENUE),
                TabModel(type = TabType.ORDER_COUNT),
                TabModel(type = TabType.GUEST_COUNT),
                TabModel(type = TabType.AVERAGE_CHECK),
            ),
            chartType = ChartType.DONUT,
            chartData = parsedData
        )
    }

    private fun List<GetAllPaySystemsByStoreIdQuery.AllPaySystemsByStoreId>.getIconResourceById(
        id: UUID
    ): Int? {
        val candidate = find { it.fragments.paySystemFragment.id == id } ?: return null
        return when(candidate.fragments.paySystemFragment.icon) {
            PaySystemIcon.CARD -> R.drawable.card
            PaySystemIcon.CASH -> R.drawable.cash
            else -> R.drawable.ruble
        }
    }

    private suspend fun getPopularProduct(): AnalyticsWidgetModel {
        val dateFrom: LocalDate = LocalDate.of(2023, 10, 1)
        val dateTo: LocalDate = LocalDate.now()
        val terminal = repository.getTerminal().first()

        val auth = authUseCase.get() ?: throw Exception()

        val client = erpClientService.principalClient(
            token = auth.accessToken,
            organizationId = terminal!!.organization.id
        ).first()

        val response = client.query(
            GetMostPopularProductsQuery(
                input = MostPopularProductsWidgetInput(
                    dateRange = DateRangeInput(dateFrom, dateTo),
                    storeIds = Input.fromNullable(listOf(terminal.storeId.value))
                )
            )
        )
        .apiFlow()
        .first().data

        val parsedData = response?.mostPopularProductsWidget?.elements?.mapIndexed { index, mostPopularProduct ->
            ListChartDataModel(
                name = mostPopularProduct.name,
                data = listOf(
                    mostPopularProduct.quantity.toInt(),
                    mostPopularProduct.revenue.toInt(),
                    mostPopularProduct.profit.toInt(),
                ),
                number = index + 1
            )
        } ?: throw Exception(EMPTY_POPULAR_PRODUCTS_REPORT_RESPONSE)

        return AnalyticsWidgetModel(
            title = ANALYTICS_TITLE_PRODUCTS,
            dateRange = "$dateFrom - $dateTo",
            tabs = listOf(
                TabModel(type = TabType.SALES),
                TabModel(type = TabType.REVENUE),
                TabModel(type = TabType.PROFIT),
            ),
            chartType = ChartType.LIST,
            chartData = parsedData
        )
    }

    private suspend fun getPopularCategories(): AnalyticsWidgetModel {
        val dateFrom: LocalDate = LocalDate.of(2023, 10, 1)
        val dateTo: LocalDate = LocalDate.now()
        val terminal = repository.getTerminal().first()

        val auth = authUseCase.get() ?: throw Exception()

        val client = erpClientService.principalClient(
            token = auth.accessToken,
            organizationId = terminal!!.organization.id
        ).first()

        val response = client.query(
            GetMostPopularCategoriesQuery(
                input = MostPopularCategoriesWidgetInput(
                    dateRange = DateRangeInput(dateFrom, dateTo),
                    storeIds = Input.fromNullable(listOf(terminal.storeId.value))
                )
            )
        )
            .apiFlow()
            .first().data

        Log.d( "test2:::","$response")

        val parsedData = response
            ?.mostPopularCategoriesWidget
            ?.elements
            ?.mapIndexed { index, mostPopularCategories ->
            ListChartDataModel(
                name = mostPopularCategories.name,
                data = listOf(
                    mostPopularCategories.quantity.toInt(),
                    mostPopularCategories.revenue.toInt(),
                    mostPopularCategories.profit.toInt(),
                ),
                number = index + 1
            )
        } ?: throw Exception(EMPTY_POPULAR_CATEGORIES_REPORT_RESPONSE)

        return AnalyticsWidgetModel(
            title = ANALYTICS_TITLE_CATEGORY,
            dateRange = "$dateFrom - $dateTo",
            tabs = listOf(
                TabModel(type = TabType.SALES),
                TabModel(type = TabType.REVENUE),
                TabModel(type = TabType.PROFIT),
            ),
            chartType = ChartType.LIST,
            chartData = parsedData
        )
    }

    private suspend fun getSalesByWeekDay(): AnalyticsWidgetModel {
        val dateFrom: LocalDate = LocalDate.of(2023, 10, 1)
        val dateTo: LocalDate = LocalDate.now()
        val terminal = repository.getTerminal().first()

        val auth = authUseCase.get() ?: throw Exception()

        val client = erpClientService.principalClient(
            token = auth.accessToken,
            organizationId = terminal!!.organization.id
        ).first()

        val response = client.query(
            GetSalesByWeekDayQuery(
                widgetInput = SalesByWeekDayWidgetInput(
                    dateRange = DateRangeInput(dateFrom, dateTo),
                    storeIds = Input.fromNullable(listOf(terminal.storeId.value))
                )
            )
        )
            .apiFlow()
            .first().data

        Log.d( "test2:::","$response")

        val parsedData = response
            ?.salesByWeekDayWidget
            ?.elements
            ?.mapIndexed { index, salesByWeekDayWidget ->
                BarChartDataModel(
                    WeekDay = salesByWeekDayWidget.dayOfWeek.toString(),
                    data = listOf(
                        salesByWeekDayWidget.averageWeeklyRevenue.toInt(),
                        salesByWeekDayWidget.averageWeeklyProfit.toInt(),
                        salesByWeekDayWidget.totalWeeklyChecksCount,
                        salesByWeekDayWidget.totalWeeklyGuestsCount,
                        salesByWeekDayWidget.averageWeeklyCheck.toInt(),
                    ),
                )
            } ?: throw Exception(EMPTY_SALES_BY_WEEK_DAY_REPORT_RESPONSE)
        Log.d("test2:::", "$parsedData")

        return AnalyticsWidgetModel(
            title = ANALYTICS_TITLE_SALES_BY_WEEK_DAY,
            dateRange = "$dateFrom - $dateTo",
            tabs = listOf(
                TabModel(type = TabType.REVENUE),
                TabModel(type = TabType.PROFIT),
                TabModel(type = TabType.ORDER_COUNT),
                TabModel(type = TabType.GUEST_COUNT),
                TabModel(type = TabType.AVERAGE_CHECK),
            ),
            chartType = ChartType.BAR,
            chartData = parsedData
        )
    }
}