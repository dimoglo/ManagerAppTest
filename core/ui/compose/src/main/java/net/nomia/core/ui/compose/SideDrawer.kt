package net.nomia.core.ui.compose

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BoxScope.SideDrawer(
    enable: Boolean,
    width: Dp,
    mode: SideDrawerMode,
    modifier: Modifier = Modifier,
    colorBackground: Color = Color.Transparent,
    content: @Composable BoxScope.() -> Unit = {},
) {
    var isShowed by remember { mutableStateOf(enable) }

    LaunchedEffect(enable) {
        if (enable) isShowed = true
    }

    val offset by animateDpAsState(
        targetValue = if (enable) {
            START_OFFSET.dp
        } else {
            when (mode) {
                SideDrawerMode.LEFT -> -width
                SideDrawerMode.RIGHT -> width
            }
        },
        finishedListener = {
            if (!enable) isShowed = false
        }
    )

    if (isShowed) {
        Box(
            modifier = modifier
                .fillMaxHeight()
                .align(
                    when (mode) {
                        SideDrawerMode.LEFT -> Alignment.CenterStart
                        SideDrawerMode.RIGHT -> Alignment.CenterEnd
                    }
                )
                .width(width)
                .offset(x = offset)
                .background(color = colorBackground)
        ) {
            content()
        }
    }
}

private const val START_OFFSET = 0

enum class SideDrawerMode {
    LEFT,
    RIGHT
}

@Preview
@Composable
private fun LeftPreview() {
    var isOpen by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Red)
    ) {
        Button(
            modifier = Modifier.align(Alignment.CenterEnd),
            onClick = { isOpen = !isOpen }
        ) {
            Text(text = "Switch")
        }
        SideDrawer(
            enable = isOpen,
            width = 250.dp,
            mode = SideDrawerMode.LEFT,
            colorBackground = Color.Green
        )
    }
}

@Preview
@Composable
private fun RightPreview() {
    var isOpen by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Red)
    ) {
        Button(
            modifier = Modifier.align(Alignment.CenterStart),
            onClick = { isOpen = !isOpen }
        ) {
            Text(text = "Switch")
        }
        SideDrawer(
            enable = isOpen,
            width = 250.dp,
            mode = SideDrawerMode.RIGHT,
            colorBackground = Color.Green
        )
    }
}
