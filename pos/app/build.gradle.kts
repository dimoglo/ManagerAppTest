import net.nomia.gradle.AppGradleExtension.applyComposeConfig
import net.nomia.gradle.GradleExtension.getGeneralKotlinConfigure
import net.nomia.gradle.Version
import java.util.*

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.androidApp.get().pluginId)
    id(libs.plugins.kotlinAndroid.get().pluginId)
    id(libs.plugins.kotlinKapt.get().pluginId)
    id(libs.plugins.hilt.get().pluginId)
    id(libs.plugins.kotlinParcelize.get().pluginId)
    alias(libs.plugins.ksp)
    alias(libs.plugins.serialization)
    alias(libs.plugins.apollo)
}
apply<Version>()

android {
    namespace = "net.nomia.pos"
    compileSdk = 33

    defaultConfig {
        applicationId = "net.nomia.pos"
        minSdk = 26
        compileSdk = 33
        versionCode = 1
        versionName = "1.0"

        multiDexEnabled = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    applyComposeConfig(
        versionCompose = libs.versions.compose.get()
    )
    kotlinOptions(
        configure = getGeneralKotlinConfigure()
    )
    packaging {
        resources.excludes.add("META-INF/DEPENDENCIES")
        resources.excludes.add("META-INF/INDEX.LIST")
        resources.excludes.add("META-INF/kotlinx_coroutines_core.version")
        resources.excludes.add("META-INF/LICENSE")
        resources.excludes.add("META-INF/LICENSE.txt")
        resources.excludes.add("META-INF/LICENSE.md")
        resources.excludes.add("META-INF/LICENSE-notice.md")
        resources.excludes.add("META-INF/license.txt")
        resources.excludes.add("META-INF/NOTICE")
        resources.excludes.add("META-INF/NOTICE.txt")
        resources.excludes.add("META-INF/notice.txt")
        resources.excludes.add("META-INF/ASL2.0")
        resources.excludes.add("META-INF/*.kotlin_module")
        resources.excludes.add("about.*")
        resources.excludes.add("logback.xml")
        resources.excludes.add("modeling32.png")
        resources.excludes.add("plugin.properties")
        resources.excludes.add("javamoney.properties")
    }
}

hilt {
    enableAggregatingTask = true
}

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(project(mapOf("path" to ":pos:erp-api")))
    implementation(project(mapOf("path" to ":pos:erp-schema")))
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.bundles.compose)
    implementation(libs.hilt.navigation.compose) {
        exclude(group = "androidx.navigation", module = "navigation-compose")
    }

    implementation(project(":core:core"))
    implementation(project(":core:common:utils"))
    implementation(project(":core:ui:preview"))
    implementation(project(":common-ui"))
    implementation(project(":core:common:network"))
    implementation(project(":pos:main"))
    implementation(project(":settings"))
    implementation(project(":core:mvi-android"))
    implementation(project(":core:common:state-handler"))
    implementation(project(":core:common:serializer"))

    implementation(libs.accompanist.insets)
    implementation(libs.accompanist.flowlayout)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.splashScreen)
    implementation(libs.bundles.activity)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.playCore)
    implementation(libs.revealswipe)
    implementation(libs.dagger)
    kapt(libs.daggerCompiler)
    kapt(libs.hiltCompiler)
    implementation(libs.core)
    implementation(libs.fragment)
    implementation(libs.deviceNames)
    coreLibraryDesugaring(libs.desugar)
    implementation(libs.material)
    implementation(libs.kotlinJson)
    implementation(libs.composeReorderable)

    implementation(libs.bundles.room)
    kapt(libs.roomCompiler)

    implementation(libs.composeAnnotation)
    ksp(libs.composeAnnotationProcessor)
    implementation(libs.ksp)
    implementation(libs.gson)
    implementation(libs.timber)
    implementation(libs.xlog)
    implementation(libs.timber)
    implementation(libs.kstatemachine)
    implementation(libs.moneta) {
        exclude(group = "javax.annotation")
    }
    implementation(libs.coil)

    implementation(libs.toolargetool)

    implementation(project(":core:core"))
    implementation(project(":core:common:network"))
    implementation(project(":core:common:utils"))
    implementation(project(":core:ui:preview"))
    implementation(project(":common-ui"))
    implementation(project(":pos:main"))
    implementation(project(":settings"))
    implementation(project(":core:mvi-android"))
    implementation(project(":core:common:state-handler"))
    implementation(project(":core:common:serializer"))
    implementation(project(":core:common:converter"))
    implementation(project(":core:ui:compose"))

    implementation(libs.bundles.apollo)
    implementation(libs.sentry.apollo)

    implementation(libs.jwt)

    // YCharts (графики)
    implementation ("co.yml:ycharts:2.1.0")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

}

kapt {
    correctErrorTypes = true
}

androidComponents.onVariants {variant ->
    kotlin.sourceSets.findByName(variant.name)?.kotlin?.srcDirs(
        file("$buildDir/generated/ksp/${variant.name}/kotlin")
    )
}

ksp {
    arg("ignoreGenericArgs", "false")
}
