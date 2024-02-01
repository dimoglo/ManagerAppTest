package net.nomia.settings.domain.model

import net.nomia.common.data.model.Theme

class AppearanceData(
    val theme: Theme,
    val useCustomTheme: Boolean,
    val showSectionImages: Boolean,
    val showItemImages: Boolean,
    val showSectionColors: Boolean,
)
