@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.kotlin.get().pluginId)
}

dependencies {
    api(libs.bundles.measure)
    api(libs.libphonenumber)
    api(libs.kotlinxCoroutines)
}
