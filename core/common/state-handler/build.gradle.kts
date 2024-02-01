import net.nomia.gradle.LibraryGradleExtension.applyDefaultConfig
import net.nomia.gradle.GradleExtension.getGeneralKotlinConfigure

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
    id(libs.plugins.kotlinAndroid.get().pluginId)
    id(libs.plugins.kotlinKapt.get().pluginId)
    id(libs.plugins.hilt.get().pluginId)
}

android {
    applyDefaultConfig(
        namespace = "net.nomia.core.common.state_handler",
    )
    kotlinOptions(
        configure = getGeneralKotlinConfigure()
    )
}

dependencies {
    implementation(libs.bundles.lifecycle)
    implementation(libs.dagger)
    kapt(libs.daggerCompiler)
    kapt(libs.hiltCompiler)
    implementation(libs.core)
}
