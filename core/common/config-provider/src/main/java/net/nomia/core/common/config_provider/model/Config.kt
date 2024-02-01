package net.nomia.core.common.config_provider.model

data class Config(
    val displayType: DisplayType,
    val themeType: ThemeType,
) {
    enum class DisplayType {
        Phone,
        Tablet;
    }

    enum class ThemeType {
        System,
        Dark,
        Light;

        fun isDark(): Boolean? = when (this) {
            System -> null
            Dark -> true
            Light -> false
        }
    }
}
