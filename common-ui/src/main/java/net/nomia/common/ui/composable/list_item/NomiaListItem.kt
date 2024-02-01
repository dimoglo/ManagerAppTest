package net.nomia.common.ui.composable.list_item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import net.nomia.common.ui.R
import net.nomia.common.ui.composable.PreviewWrapper
import net.nomia.common.ui.extensions.clickableNoIndication
import net.nomia.common.ui.previews.ThemePreviews
import net.nomia.common.ui.theme.spacers

@Suppress("MagicNumber")
@Composable
fun NomiaListItem(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selected: Boolean = false,
    colors: NomiaListItemColors = NomiaListItemDefault.colors(enabled = enabled),
    headlineText: @Composable () -> Unit,
    extraHeadlineText: @Composable (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    extraSupportingText: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
) {
    if (supportingText == null) {
        NomiaListItem(
            modifier = modifier,
            containerColor = if (selected) colors.selectedColor else colors.containerColor,
            contentColor = colors.headlineColor,
            minHeight = ListItemOneLineHeight,
        ) {
            if (leadingContent != null) {
                LeadingContent(
                    contentColor = colors.leadingIconColor,
                    content = leadingContent
                )
            }

            val (headlineModifier, extraHeadlineModifier) = if (extraHeadlineText != null)
                Modifier.weight(0.5F) to Modifier.weight(0.5F)
            else
                Modifier.weight(1.0F) to Modifier

            Box(modifier = headlineModifier) {
                ProvideTextStyle(MaterialTheme.typography.titleMedium) {
                    headlineText()
                }
            }

            if (extraHeadlineText != null) {
                ExtraHeadlineContent(
                    modifier = extraHeadlineModifier,
                    contentColor = colors.extraHeadlineColor,
                    content = extraHeadlineText
                )
            }

            if (trailingContent != null) {
                TrailingContent(
                    contentColor = colors.trailingIconColor,
                    content = trailingContent
                )
            }
        }
    } else {
        NomiaListItem(
            modifier = modifier,
            containerColor = if (selected) colors.selectedColor else colors.containerColor,
            contentColor = colors.headlineColor,
            minHeight = ListItemTwoLinesHeight
        ) {
            if (leadingContent != null) {
                LeadingContent(
                    contentColor = colors.leadingIconColor,
                    content = leadingContent
                )
            }

            Column(modifier = Modifier.weight(1.0F)) {
                ProvideTextStyle(MaterialTheme.typography.titleMedium) {
                    headlineText()
                }
                ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
                    CompositionLocalProvider(LocalContentColor provides colors.supportingTextColor) {
                        supportingText()
                    }
                }
            }

            if (extraHeadlineText != null) {
                ExtraHeadlineContent(
                    contentColor = colors.extraHeadlineColor,
                    supportingContentColor = colors.supportingTextColor,
                    content = extraHeadlineText,
                    supportingContent = extraSupportingText,
                )
            }

            if (trailingContent != null) {
                TrailingContent(
                    contentColor = colors.trailingIconColor,
                    content = trailingContent
                )
            }
        }
    }
}

@Composable
fun NomiaNavigateListItem(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selected: Boolean = false,
    colors: NomiaListItemColors = NomiaListItemDefault.colors(enabled = enabled),
    headlineText: @Composable () -> Unit,
    extraHeadlineText: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    NomiaListItem(
        modifier = Modifier
            .clickable(onClick = onClick, enabled = enabled)
            .then(modifier),
        enabled = enabled,
        selected = selected,
        colors = colors,
        headlineText = headlineText,
        leadingContent = leadingContent,
        extraHeadlineText = extraHeadlineText,
        supportingText = supportingText,
        trailingContent = {
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_right_24),
                contentDescription = null,
            )
        }
    )
}

@Composable
fun NomiaCheckboxListItem(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    checked: Boolean = false,
    colors: NomiaListItemColors = NomiaListItemDefault.colors(enabled = enabled),
    headlineText: @Composable () -> Unit,
    extraHeadlineText: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    onClick: (Boolean) -> Unit
) {
    NomiaListItem(
        headlineText = headlineText,
        modifier = modifier.clickableNoIndication(enabled = enabled, onClick = { onClick(!checked) }),
        enabled = enabled,
        colors = colors,
        extraHeadlineText = extraHeadlineText,
        leadingContent = {
            Checkbox(
                modifier = Modifier.requiredSize(MaterialTheme.spacers.large),
                checked = checked,
                onCheckedChange = { onClick(it) },
                enabled = enabled
            )
        },
        supportingText = supportingText,
        trailingContent = trailingContent,
    )
}

@Composable
fun NomiaSwitchListItem(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    checked: Boolean = false,
    colors: NomiaListItemColors = NomiaListItemDefault.colors(enabled = enabled),
    headlineText: @Composable () -> Unit,
    leadingContent: @Composable (() -> Unit)? = null,
    extraHeadlineText: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    onClick: (Boolean) -> Unit,
) {
    NomiaListItem(
        headlineText = headlineText,
        modifier = modifier.clickableNoIndication(enabled = enabled, onClick = { onClick(checked) }),
        enabled = enabled,
        colors = colors,
        extraHeadlineText = extraHeadlineText,
        leadingContent = leadingContent,
        supportingText = supportingText,
        trailingContent = {
            Switch(
                modifier = Modifier.requiredHeight(MaterialTheme.spacers.extraLarge),
                enabled = enabled,
                checked = checked,
                onCheckedChange = onClick
            )
        },
    )

}

@Composable
fun NomiaRadioButtonListItem(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selected: Boolean = false,
    colors: NomiaListItemColors = NomiaListItemDefault.colors(enabled = enabled),
    headlineText: @Composable () -> Unit,
    extraHeadlineText: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    onClick: () -> Unit,
) {
    NomiaListItem(
        headlineText = headlineText,
        modifier = modifier.clickableNoIndication(enabled = enabled, onClick = onClick),
        enabled = enabled,
        colors = colors,
        extraHeadlineText = extraHeadlineText,
        leadingContent = {
            RadioButton(
                modifier = Modifier.requiredSize(MaterialTheme.spacers.large),
                selected = selected,
                onClick = onClick,
            )
        },
        supportingText = supportingText,
        trailingContent = trailingContent,
    )
}

@Composable
private fun NomiaListItem(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    tonalElevation: Dp = NomiaListItemDefault.elevation,
    shadowElevation: Dp = NomiaListItemDefault.elevation,
    paddingValues: PaddingValues = PaddingValues(vertical = ListItemVerticalPadding),
    minHeight: Dp,
    content: @Composable RowScope.() -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
    ) {
        Row(
            modifier = Modifier
                .heightIn(min = minHeight)
                .padding(paddingValues),
            content = content
        )
    }
}

@Composable
private fun RowScope.ExtraHeadlineContent(
    modifier: Modifier = Modifier,
    contentColor: Color,
    supportingContentColor: Color = Color.Unspecified,
    content: @Composable () -> Unit,
    supportingContent: @Composable (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .padding(start = MaterialTheme.spacers.medium)
            .align(Alignment.CenterVertically),
        horizontalAlignment = Alignment.End,
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides MaterialTheme.typography.bodyLarge,
            LocalContentColor provides contentColor,
        ) {
            content()
        }

        supportingContent?.let {
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.bodyMedium,
                LocalContentColor provides supportingContentColor
            ) {
                supportingContent()
            }
        }
    }
}

@Composable
private fun RowScope.LeadingContent(
    contentColor: Color,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Box(
            modifier = Modifier
                .padding(end = MaterialTheme.spacers.medium)
                .align(Alignment.CenterVertically)
        ) {
            content()
        }
    }
}

@Composable
private fun RowScope.TrailingContent(
    contentColor: Color,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Box(
            modifier = Modifier
                .padding(start = MaterialTheme.spacers.medium)
                .align(Alignment.CenterVertically)
        ) {
            content()
        }
    }
}

object NomiaListItemDefault {
    val elevation = 0.dp
    private const val disabledOpacity = 0.38F
    private const val selectedOpacity = 0.12F

    @Composable
    fun colors(enabled: Boolean) =
        NomiaListItemColors(
            containerColor = MaterialTheme.colorScheme.surface,
            headlineColor = with(MaterialTheme.colorScheme) {
                if (enabled) onSurface else onSurface.copy(alpha = disabledOpacity)
            },
            extraHeadlineColor = with(MaterialTheme.colorScheme) {
                if (enabled) onSurface else onSurface.copy(alpha = disabledOpacity)
            },
            leadingIconColor = with(MaterialTheme.colorScheme) {
                if (enabled) onSurfaceVariant else onSurfaceVariant.copy(
                    alpha = disabledOpacity
                )
            },
            supportingTextColor = with(MaterialTheme.colorScheme) {
                if (enabled) onSurfaceVariant else onSurfaceVariant.copy(
                    alpha = disabledOpacity
                )
            },
            trailingIconColor = with(MaterialTheme.colorScheme) {
                if (enabled) onSurfaceVariant else onSurfaceVariant.copy(
                    alpha = disabledOpacity
                )
            },
            selectedColor = MaterialTheme.colorScheme.primary.copy(selectedOpacity)
        )
}

data class NomiaListItemColors(
    val containerColor: Color,
    val headlineColor: Color,
    val extraHeadlineColor: Color,
    val leadingIconColor: Color,
    val supportingTextColor: Color,
    val trailingIconColor: Color,
    val selectedColor: Color,
)

private val ListItemVerticalPadding = 12.dp
private val ListItemOneLineHeight = 24.dp
private val ListItemTwoLinesHeight = 64.dp

@Suppress("UnusedPrivateMember")
@ThemePreviews
@Composable
private fun NomiaListItemPreviews(
    @PreviewParameter(NomiaListItemPreviewProvider::class) data: NomiaListItemPreviewProviderData
) {
    PreviewWrapper {
        Column(Modifier.width(400.dp)) {
            with(data) {
                val headlineContent: @Composable () -> Unit = { Text(text = stringResource(id = headlineTextResId)) }
                val supportingContent: @Composable () -> Unit =
                    { Text(text = stringResource(id = supportingTextResId)) }
                val extraHeadlineContent: @Composable () -> Unit =
                    { Text(text = stringResource(id = extraHeadlineTextResId)) }
                val leadingContent: @Composable () -> Unit = {
                    Icon(
                        painter = painterResource(id = leadingIconResId),
                        contentDescription = null
                    )
                }
                val trailingContent: @Composable () -> Unit = {
                    Icon(
                        painter = painterResource(id = trailingIconResId),
                        contentDescription = null
                    )
                }

                NomiaListItem(headlineText = headlineContent)
                Divider()
                NomiaListItem(headlineText = headlineContent, leadingContent = leadingContent)
                Divider()
                NomiaListItem(headlineText = headlineContent, trailingContent = trailingContent)
                Divider()
                NomiaListItem(
                    headlineText = headlineContent,
                    leadingContent = leadingContent,
                    trailingContent = trailingContent
                )
                Divider()
                NomiaListItem(
                    headlineText = headlineContent,
                    extraHeadlineText = extraHeadlineContent,
                    leadingContent = leadingContent,
                    trailingContent = trailingContent
                )
                Divider()
                NomiaListItem(
                    headlineText = headlineContent,
                    supportingText = supportingContent
                )
                Divider()
                NomiaListItem(
                    headlineText = headlineContent,
                    leadingContent = leadingContent,
                    supportingText = supportingContent
                )
                Divider()
                NomiaListItem(
                    headlineText = headlineContent,
                    trailingContent = trailingContent,
                    supportingText = supportingContent
                )
                Divider()
                NomiaListItem(
                    headlineText = headlineContent,
                    trailingContent = trailingContent,
                    leadingContent = leadingContent,
                    supportingText = supportingContent
                )
                Divider()
                NomiaListItem(
                    headlineText = headlineContent,
                    extraHeadlineText = extraHeadlineContent,
                    trailingContent = trailingContent,
                    leadingContent = leadingContent,
                    supportingText = supportingContent
                )
                Divider()
                NomiaListItem(
                    enabled = false,
                    headlineText = headlineContent,
                    extraHeadlineText = extraHeadlineContent,
                    trailingContent = trailingContent,
                    leadingContent = leadingContent,
                    supportingText = supportingContent
                )
            }
        }
    }
}
