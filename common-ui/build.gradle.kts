import net.nomia.gradle.RoomExportSchemeProperties
import net.nomia.gradle.LibraryGradleExtension.applyComposeConfig
import net.nomia.gradle.LibraryGradleExtension.applyDefaultConfig
import net.nomia.gradle.GradleExtension.getGeneralKotlinConfigure

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
    id(libs.plugins.kotlinAndroid.get().pluginId)
    id(libs.plugins.kotlinKapt.get().pluginId)
    id(libs.plugins.kotlinParcelize.get().pluginId)
}

android {
    applyDefaultConfig(
        namespace = "net.nomia.common.ui",
        roomExportSchemeProperties = RoomExportSchemeProperties(projectDir),
        desugaringEnabled = true
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
    implementation(libs.libphonenumber)
    implementation(libs.lottie)
    implementation(libs.kotlinxCoroutines)
    implementation(libs.composeReorderable)
    coreLibraryDesugaring(libs.desugar)


    implementation(project(":pos:common-data"))
    implementation(project(":settings"))
    implementation(project(":core:core"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.testRunner)
    androidTestImplementation(libs.testRules)
    androidTestImplementation(libs.testExtJunit)
    androidTestImplementation(libs.testRobolectric)
    androidTestImplementation(libs.testCoroutines)
    androidTestImplementation(libs.testCore)
    testImplementation(libs.testCoroutines)
}
