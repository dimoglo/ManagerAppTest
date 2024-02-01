import net.nomia.gradle.SdkVersionProperties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("java")
    id(libs.plugins.jvm.get().pluginId)
}

dependencies {
    implementation(libs.kotlinxCoroutines)
}

java {
    sourceCompatibility = SdkVersionProperties.Companion.DEFAULT.javaVersion
    targetCompatibility = SdkVersionProperties.Companion.DEFAULT.javaVersion
}
