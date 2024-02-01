package net.nomia.pos.ui.analytics.charts

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.nomia.pos.domain.analytics.AnalyticsWidgetModel
import net.nomia.pos.domain.analytics.ChartType
import net.nomia.pos.domain.analytics.charts.DonutChartDataModel
import net.nomia.pos.domain.analytics.charts.ListChartDataModel
import net.nomia.pos.ui.analytics.charts.common.HeaderForChart
import net.nomia.pos.ui.analytics.charts.common.TabsForChart
import net.nomia.pos.ui.analytics.charts.donut.DonutChart
import net.nomia.pos.ui.analytics.charts.list.ListChart

@Composable
internal fun AnalyticsWidget (
    analyticsWidgetModel: AnalyticsWidgetModel,
    modifier: Modifier
) {
    var selectedTab by remember { mutableStateOf(analyticsWidgetModel.tabs.first()) }

    Column (
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(2.dp, Color.Gray, shape = RoundedCornerShape(20.dp))
    ){
        HeaderForChart(
            text = analyticsWidgetModel.title,
            modifier = Modifier
        )
        Divider(
            color = Color.Gray,
            thickness = 2.dp,
        )
        TabsForChart(tabs = analyticsWidgetModel.tabs, onTabSelected = { tab ->
            selectedTab = tab
        })

        val selectedTabIndex = analyticsWidgetModel.tabs.indexOf(selectedTab)
        when(analyticsWidgetModel.chartType) {
            ChartType.DONUT -> DonutChart(
                data = analyticsWidgetModel.chartData as List<DonutChartDataModel>,
                selectedTabIndex = selectedTabIndex
            )
            ChartType.LIST -> ListChart(
                data = analyticsWidgetModel.chartData as List<ListChartDataModel>,
                selectedTabIndex = selectedTabIndex,
                selectedTab = selectedTab
            )
//            ChartType.LINE -> LineChart(
//                data = analyticsWidgetModel.chartData as List<LineChartDataModel>,
//                selectedTabIndex = selectedTabIndex
//            )
//            ChartType.BAR -> BarChart(
//                data = analyticsWidgetModel.chartData as List<BarChartDataModel>,
//                selectedTabIndex = selectedTabIndex
//            )
            else -> Text(text = "TODO")
        }
    }
}