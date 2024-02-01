package net.nomia.pos.ui.analytics.charts.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.nomia.pos.domain.analytics.TabModel
import net.nomia.pos.domain.analytics.TabType
import net.nomia.pos.domain.analytics.charts.ListChartDataModel

@Composable
fun ListChart(
    data: List<ListChartDataModel>,
    selectedTabIndex: Int,
    selectedTab: TabModel
){
    Column(
        modifier = Modifier
            .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
    ){
        data.forEach { listChartData ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = "${listChartData.number}.",
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                )
                Spacer(modifier = Modifier.size(12.dp))
                Text(
                    text = listChartData.name,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                )
                
                Spacer(modifier = Modifier.weight(1f))
//                val hasSuffix = TabType.values().get(selectedTabIndex).hasSuffix
                val hasSuffix = TabType.valueOf(selectedTab.type.name).hasSuffix
                Text(
                    text = "${listChartData.data[selectedTabIndex]} ${if(hasSuffix) "₽" else ""}",
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                )
            }
        }
    }
}