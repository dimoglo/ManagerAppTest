package net.nomia.pos.ui.analytics.charts.donut

import android.util.Log
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.nomia.pos.domain.analytics.AnalyticsWidgetModel
import net.nomia.pos.domain.analytics.TabType
import net.nomia.pos.domain.analytics.charts.DonutChartDataModel
import kotlin.random.Random

@Composable
fun DonutChart(
    data: List<DonutChartDataModel>,
    selectedTabIndex: Int,
    radiusOuter: Dp = 80.dp,
    slicePadding: Dp = 12.dp,
    animDuration: Int = 1000,
) {

    val totalSum = data.sumOf { it.data[selectedTabIndex] }
    val floatValue = data.map {
        val value = it.data[selectedTabIndex]
        360 * value.toFloat() / totalSum.toFloat()
    }

    var animationPlayed by remember { mutableStateOf(false) }
    var startAngle = 0f

    val colors = mutableListOf<Color>()

    val animateSize by animateFloatAsState(
        targetValue = if (animationPlayed) radiusOuter.value * 2f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        ), label = ""
    )

    val animateRotation by animateFloatAsState(
        targetValue = if (animationPlayed) 360f * 3f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        ), label = ""
    )

    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Column(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        ) {

            data.forEach { value ->
                val color = colorResource(value.color)
                colors.add(color)
                DetailsPieChartItem(
                    selectedTabIndex,
                    name = value.name,
                    value = value.data[selectedTabIndex],
                    color = color,
                    iconResource = value.icon!!
                )
            }

        }

        Box(
            modifier = Modifier
                .wrapContentSize()
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            val strokeWidth = radiusOuter.value * 0.5f
            val padding = strokeWidth * 0.5f
            val canvasSize = radiusOuter.value * 2f + padding
            Canvas(
                modifier = Modifier
                    .size(canvasSize.dp)
                    .padding(padding.dp)
                    .rotate(animateRotation),
            ) {
                floatValue.forEachIndexed { index, angle ->
                    drawArc(
                        color = colors[index],
                        startAngle + (slicePadding / 2).value,
                        angle - (slicePadding / 2).value,
                        useCenter = false,
                        style = Stroke(strokeWidth.dp.toPx())
                    )
                    startAngle += angle
                }
            }
        }
    }
}

@Composable
fun DetailsPieChartItem(
    selectedTabIndex: Int,
    value: Int,
    name: String,
    color: Color,
    iconResource: Int
) {
    Surface(
        modifier = Modifier
            .padding(vertical = 5.dp),
        color = Color.Transparent
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .width(10.dp)
                    .height(35.dp)
                    .background(
                        color = color,
                        shape = RoundedCornerShape(20.dp)//cornerRadius
                    )
            )
            Spacer(Modifier.width(8.dp))
            Icon(
                painter = painterResource(id = iconResource),
                contentDescription = name,
                modifier = Modifier
                    .size(35.dp)
                    .padding(end = 8.dp),
                tint = Color.Gray
            )

            Text(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                text = name,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
            )

            val hasSuffix = TabType.values().get(selectedTabIndex).hasSuffix

            Text(
                text = "$value ${if(hasSuffix) "₽" else ""}",
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
            )
        }
    }
}

