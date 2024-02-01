@file:OptIn(ExperimentalAnimationApi::class)

package net.nomia.common.ui.composable

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.SnapSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import net.nomia.common.data.Constants


@Composable
fun <S> SliderAnimatedContent(
    modifier: Modifier = Modifier,
    targetState: S,
    indexStateProvider: (S) -> Int,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable AnimatedVisibilityScope.(targetState: S) -> Unit
) {
    AnimatedContent(
        modifier = modifier,
        targetState = targetState,
        transitionSpec = {
            if (indexStateProvider(initialState) != indexStateProvider(targetState)) {
                val animationSpec: TweenSpec<IntOffset> = tween(Constants.CONTENT_ANIMATION_DURATION)
                val direction = getTransitionDirection(
                    initialIndex = indexStateProvider(initialState),
                    targetIndex = indexStateProvider(targetState),
                )
                slideIntoContainer(
                    towards = direction,
                    animationSpec = animationSpec,
                ) with slideOutOfContainer(
                    towards = direction,
                    animationSpec = animationSpec
                )
            } else {
                // if states are on the same level, hide one and open another instantly
                val animationSpec: SnapSpec<Float> = snap()
                fadeIn(animationSpec) with fadeOut(animationSpec)
            }
        },
        contentAlignment = contentAlignment,
        content = content
    )
}

@OptIn(ExperimentalAnimationApi::class)
private fun getTransitionDirection(
    initialIndex: Int,
    targetIndex: Int
): AnimatedContentScope.SlideDirection =
    if (targetIndex > initialIndex) {
        AnimatedContentScope.SlideDirection.Left
    } else {
        AnimatedContentScope.SlideDirection.Right
    }
