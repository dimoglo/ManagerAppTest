import net.nomia.gradle.LibraryGradleExtension.applyDefaultConfig
import net.nomia.gradle.GradleExtension.getGeneralKotlinConfigure
import net.nomia.gradle.RoomExportSchemeProperties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
    id(libs.plugins.kotlinAndroid.get().pluginId)
    id(libs.plugins.kotlinKapt.get().pluginId)
    alias(libs.plugins.apollo)
}

android {
    applyDefaultConfig(
        namespace = "net.nomia.main",
        roomExportSchemeProperties = RoomExportSchemeProperties(projectDir),
        desugaringEnabled = true
    )
    kotlinOptions(
        configure = getGeneralKotlinConfigure()
    )
}

dependencies {
    implementation(libs.jwt)
    implementation(libs.kotlinxCoroutines)
    coreLibraryDesugaring(libs.desugar)
    implementation(libs.dagger)
    implementation(libs.timber)
    kapt(libs.daggerCompiler)
    implementation(libs.bundles.room)
    kapt(libs.roomCompiler)
    implementation(libs.paging)
    implementation(libs.bundles.apollo)

    api(project(":pos:common-data"))
    api(project(":common-ui"))
    implementation(project(":core:common:network"))
    implementation(project(":core:core"))
    apolloMetadata(project(":pos:erp-schema"))
    implementation(project(":pos:erp-schema"))
    implementation(project(":pos:erp-api"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.testRunner)
    androidTestImplementation(libs.testRules)
    androidTestImplementation(libs.testExtJunit)
    androidTestImplementation(libs.testRobolectric)
    androidTestImplementation(libs.testCoroutines)
    androidTestImplementation(libs.testCoreArch)
    testImplementation(libs.testCoroutines)
}
