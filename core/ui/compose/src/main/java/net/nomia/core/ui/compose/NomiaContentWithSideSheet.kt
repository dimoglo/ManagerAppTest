package net.nomia.core.ui.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import net.nomia.common.ui.extensions.clickableNoIndication

private const val DURATION_OF_ANIMATION = 500
private const val SCRIM_ALPHA = 0.32f

@Composable
fun NomiaContentWithSideSheet(
    sideSheetOpened: Boolean,
    onSideSheetClose: () -> Unit,
    widthOfSideSheet: Dp = 400.dp,
    content: @Composable BoxWithConstraintsScope.() -> Unit,
    sideSheetContent: @Composable BoxScope.() -> Unit,
    alphaAnimationEnabled: Boolean = true,
) {
    var currentSizeOfSideSheet by remember { mutableStateOf(0f) }
    val fullSizeOfSideSheet = with(LocalDensity.current) { widthOfSideSheet.toPx() }
    var isOpened by remember { mutableStateOf(false) }

    val alpha by remember(currentSizeOfSideSheet, fullSizeOfSideSheet) {
        derivedStateOf {
            val percents = SCRIM_ALPHA * currentSizeOfSideSheet / fullSizeOfSideSheet
            if (percents > 1.0f || !isOpened) 0f else percents
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .statusBarsPadding()
            .background(
                color = if (alphaAnimationEnabled) Color.Black.copy(alpha = alpha) else Color.Transparent
            )
            .fillMaxSize()
            .let { modifier ->
                if (sideSheetOpened) modifier.then(Modifier.clickableNoIndication { onSideSheetClose() })
                else modifier
            },
    ) {
        content()

        var maxWidthPx by remember { mutableStateOf(0f) }
        with(LocalDensity.current) { maxWidthPx = maxWidth.toPx() }

        AnimatedVisibility(
            modifier = Modifier.align(Alignment.CenterEnd),
            visible = sideSheetOpened,
            enter = slideInHorizontally(
                animationSpec = tween(
                    durationMillis = DURATION_OF_ANIMATION,
                ),
                initialOffsetX = { it },
            ),
            exit = slideOutHorizontally(
                animationSpec = tween(
                    durationMillis = DURATION_OF_ANIMATION,
                ),
                targetOffsetX = { it },
            ),
        ) {
            DisposableEffect(Unit) {
                isOpened = true
                onDispose { isOpened = false }
            }

            Box(
                modifier = Modifier
                    .onGloballyPositioned { layoutCoordinates ->
                        currentSizeOfSideSheet = maxWidthPx - layoutCoordinates.boundsInRoot().left
                    }
                    .align(Alignment.CenterEnd)
                    .width(widthOfSideSheet)
                    .fillMaxHeight()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(16.dp, 0.dp, 0.dp, 16.dp),
                    )
                    .clickableNoIndication {}
                    .clip(RoundedCornerShape(bottomStart = 16.dp))
            ) {
                sideSheetContent()
            }
        }
    }
}
