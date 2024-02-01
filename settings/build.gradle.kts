import net.nomia.gradle.LibraryGradleExtension.applyDefaultConfig
import net.nomia.gradle.GradleExtension.getGeneralKotlinConfigure
import net.nomia.gradle.RoomExportSchemeProperties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
    id(libs.plugins.kotlinAndroid.get().pluginId)
    id(libs.plugins.kotlinKapt.get().pluginId)
}

android {
    applyDefaultConfig(
        namespace = "net.nomia.settings",
        roomExportSchemeProperties = RoomExportSchemeProperties(projectDir),
        desugaringEnabled = true,
        taskInDefaultConfig = {
            buildConfigField("Boolean", "KDS_INTEGRATION_ENABLED", "true")
            buildConfigField("Boolean", "BARCODE_SCANNING_ENABLED", "false")
            buildConfigField("Boolean", "POS_SAVE_LOGS_TO_FILE_ENABLED", "true")
        },
    )
    kotlinOptions(
        configure = getGeneralKotlinConfigure()
    )
}

dependencies {
    implementation(libs.kotlinxCoroutines)
    coreLibraryDesugaring(libs.desugar)
    implementation(libs.dagger)
    kapt(libs.daggerCompiler)
    implementation(libs.bundles.room)
    kapt(libs.roomCompiler)
    implementation(libs.paging)
    implementation(libs.jwt)

    api(project(":pos:common-data"))
    implementation(project(":core:core"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.testRunner)
    androidTestImplementation(libs.testRules)
    androidTestImplementation(libs.testExtJunit)
    androidTestImplementation(libs.testRobolectric)
    androidTestImplementation(libs.testCoroutines)
    androidTestImplementation(libs.testCoreArch)
    testImplementation(libs.testCoroutines)
}
