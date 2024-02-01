import net.nomia.gradle.LibraryGradleExtension.applyComposeConfig
import net.nomia.gradle.LibraryGradleExtension.applyDefaultConfig
import net.nomia.gradle.GradleExtension.getGeneralKotlinConfigure

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
    id(libs.plugins.kotlinAndroid.get().pluginId)
}

android {
    applyDefaultConfig(
        namespace = "net.nomia.mvi",
        desugaringEnabled = true,
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
    coreLibraryDesugaring(libs.desugar)
    implementation(libs.core)
    implementation(libs.activity.appcompat)
    implementation(libs.material)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.compose)

    api(project(":core:mvi-coroutines"))
}
