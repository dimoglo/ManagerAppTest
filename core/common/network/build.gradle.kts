import net.nomia.gradle.LibraryGradleExtension.applyDefaultConfig
import net.nomia.gradle.GradleExtension.getGeneralKotlinConfigure

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
    id(libs.plugins.kotlinAndroid.get().pluginId)
    id(libs.plugins.kotlinKapt.get().pluginId)
    alias(libs.plugins.apollo)
}

android {
    applyDefaultConfig(
        namespace = "net.nomia.common.network",
        desugaringEnabled = true
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

    testImplementation(libs.junit)
    androidTestImplementation(libs.testRunner)
    androidTestImplementation(libs.testRules)
    androidTestImplementation(libs.testExtJunit)
    androidTestImplementation(libs.testRobolectric)
    androidTestImplementation(libs.testCoroutines)
    androidTestImplementation(libs.testCoreArch)
    testImplementation(libs.testCoroutines)
}
