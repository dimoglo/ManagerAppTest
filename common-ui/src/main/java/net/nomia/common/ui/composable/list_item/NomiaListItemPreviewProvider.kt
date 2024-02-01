package net.nomia.common.ui.composable.list_item

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import net.nomia.common.ui.R

internal class NomiaListItemPreviewProvider : PreviewParameterProvider<NomiaListItemPreviewProviderData> {
    override val values: Sequence<NomiaListItemPreviewProviderData>
        get() = sequenceOf(
            NomiaListItemPreviewProviderData(
                headlineTextResId = R.string.preview_headline_text,
                supportingTextResId = R.string.preview_support_text,
                extraHeadlineTextResId = R.string.preview_extra_headline_text,
                leadingIconResId = R.drawable.ic_misc_24,
                trailingIconResId = R.drawable.ic_chevron_right_24
            )
        )
}

internal class NomiaListItemPreviewProviderData(
    @StringRes
    val headlineTextResId: Int,
    @StringRes
    val supportingTextResId: Int,
    @StringRes
    val extraHeadlineTextResId: Int,
    @DrawableRes
    val leadingIconResId: Int,
    @DrawableRes
    val trailingIconResId: Int,
)
