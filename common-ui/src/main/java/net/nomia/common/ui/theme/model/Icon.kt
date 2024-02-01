package net.nomia.common.ui.theme.model

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

val ArrowDropUp: ImageVector
    get() {
        if (arrowDropUp != null) {
            return arrowDropUp!!
        }
        arrowDropUp = materialIcon(name = "Filled.ArrowDropUp") {
            materialPath {
                moveTo(7.0f, 14.0f)
                lineToRelative(5.0f, -5.0f)
                lineToRelative(5.0f, 5.0f)
                close()
            }
        }
        return arrowDropUp!!
    }

private var arrowDropUp: ImageVector? = null
