package net.nomia.common.ui.composable

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import net.nomia.common.ui.theme.NomiaThemeMaterial3

@Composable
fun PreviewWrapper(content: @Composable () -> Unit) {
    NomiaThemeMaterial3 {
        Surface(
            content = content
        )
    }
}
