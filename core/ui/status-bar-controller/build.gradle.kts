import net.nomia.gradle.LibraryGradleExtension.applyComposeConfig
import net.nomia.gradle.LibraryGradleExtension.applyDefaultConfig
import net.nomia.gradle.GradleExtension.getGeneralKotlinConfigure

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
    id(libs.plugins.kotlinAndroid.get().pluginId)
    id(libs.plugins.kotlinKapt.get().pluginId)
}

android {
    applyDefaultConfig(
        namespace = "net.nomia.core.ui.status_bar_controller",
    )
    applyComposeConfig(
        versionCompose = libs.versions.compose.get()
    )
    kotlinOptions(
        configure = getGeneralKotlinConfigure()
    )
}

dependencies {
    implementation(platform(libs.compose.bom))
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.bundles.compose)
    implementation(libs.core)
    implementation(libs.accompanist.systemuicontroller)
}
