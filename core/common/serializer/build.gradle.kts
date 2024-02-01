@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.kotlin.get().pluginId)
    alias(libs.plugins.serialization)
}

dependencies {
    implementation(libs.kotlinJson)
}