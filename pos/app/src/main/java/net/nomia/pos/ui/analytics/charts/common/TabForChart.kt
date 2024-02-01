package net.nomia.pos.ui.analytics.charts.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.nomia.pos.domain.analytics.TabModel

    @Composable
    fun TabsForChart(
        tabs: List<TabModel>,
        onTabSelected: (TabModel) -> Unit
    ) {

        var selectedTabIndex by remember { mutableStateOf(0) }

        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            edgePadding = 0.dp,
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = index == selectedTabIndex,
                    onClick = {
                        selectedTabIndex = index
                        onTabSelected(tab)
                    },
                ) {
                    Text(
                        text = tab.type.displayName,
                        modifier = Modifier.padding(16.dp),
                        style = TextStyle(color = Color.Gray, fontSize = 16.sp)
                    )
                }
            }
        }
    }

