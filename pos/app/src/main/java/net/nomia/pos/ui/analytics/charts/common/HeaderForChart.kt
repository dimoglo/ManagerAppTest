package net.nomia.pos.ui.analytics.charts.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import net.nomia.pos.R

@Composable
fun HeaderForChart(
    text: String,
    modifier: Modifier,
){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(70.dp)
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 16.dp)
//            .size(25.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(5.dp)
        ){
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )
            Text(
                text = "16.10.2023-22.10.2023",
                fontWeight = FontWeight.Normal,
                modifier = Modifier.align(Alignment.Start)
            )
        }
        Icon(
            painterResource(id = R.drawable.filter_by_dates),
            contentDescription = "Filter by dates",
            modifier = Modifier
                .size(40.dp)
//                    .clickable(onClick = )
                .align(Alignment.CenterVertically)
                .padding(5.dp)
        )
    }
}