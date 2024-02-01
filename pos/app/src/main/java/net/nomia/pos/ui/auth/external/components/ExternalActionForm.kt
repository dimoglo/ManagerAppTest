package net.nomia.pos.ui.auth.external.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.nomia.common.ui.composable.NomiaFilledButton

@Composable
internal fun ExternalActionForm(
    @StringRes buttonTitleResId: Int,
    onClickAction: () -> Unit,
) {
    Spacer(modifier = Modifier.height(40.dp))

    NomiaFilledButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClickAction,
    ) {
        Text(text = stringResource(id = buttonTitleResId))
    }
}
