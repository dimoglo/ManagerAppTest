@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.kotlin.get().pluginId)
}

dependencies {
    implementation(libs.kotlinxCoroutines)
}