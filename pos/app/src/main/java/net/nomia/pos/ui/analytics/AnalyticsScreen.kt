package net.nomia.pos.ui.analytics

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import net.nomia.pos.R
import net.nomia.pos.ui.analytics.charts.AnalyticsWidget

@Composable
internal fun AnalyticsScreen(
    viewModel: AnalyticsViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(vertical = 12.dp)

        ) {
            Text(
                text = "Аналитика",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.calendar_white),
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = "Navigate Back",
                modifier = Modifier
                    .size(28.dp)
            )
        }


        if (state.data.isNotEmpty())
            state.data.forEach {

                AnalyticsWidget(
                    analyticsWidgetModel = it,
                    modifier = Modifier
                        .padding(top = 10.dp)
                )

            }
        else

            CircularProgressIndicator()


    }
}
