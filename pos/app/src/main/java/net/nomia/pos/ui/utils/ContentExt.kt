package net.nomia.pos.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import net.nomia.pos.core.text.Content

@Composable
fun Content.stringValue(): String {
    return stringValue(LocalContext.current.resources)
}
