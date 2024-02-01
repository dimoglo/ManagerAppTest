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
        namespace = "net.nomia.erp.schema",
        desugaringEnabled = true
    )
    kotlinOptions(
        configure = getGeneralKotlinConfigure()
    )
    apollo {
        generateApolloMetadata.set(true)
        generateKotlinModels.set(true)
        customTypeMapping.set(
            mapOf(
                "UUID" to "java.util.UUID",
                "Long" to "kotlin.Long",
                "Void" to "java.lang.Void",
                "Instant" to "java.time.Instant",
                "BigDecimal" to "java.math.BigDecimal",
                "Boolean" to "java.lang.Boolean",
                "LocalDate" to "java.time.LocalDate",
                "LocalTime" to "java.time.LocalTime",
                "LocalDateTime" to "java.time.LocalDateTime"
            )
        )
    }

    dependencies {
        coreLibraryDesugaring(libs.desugar)
        api(libs.bundles.apollo)
        api(project(":pos:common-data"))
    }
}
