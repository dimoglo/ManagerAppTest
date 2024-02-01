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
        namespace = "net.nomia.pos.core",
        desugaringEnabled = true
    )
    kotlinOptions(
        configure = getGeneralKotlinConfigure()
    )
}


dependencies {
    implementation(libs.startup)
    implementation(libs.kotlinxCoroutines)
    coreLibraryDesugaring(libs.desugar)
    implementation(libs.dagger)
    kapt(libs.daggerCompiler)
    kapt(libs.hiltCompiler)
    implementation(libs.paging)
    implementation(libs.bundles.room)
    implementation(libs.timber)
    kapt(libs.roomCompiler)
    testImplementation(libs.junit)
    implementation(libs.kotlinReflect)

    api(project(":pos:common-data"))
}
