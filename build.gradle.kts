import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import net.nomia.gradle.SdkVersionProperties
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        google()
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots")
            content {
                includeModule(
                    libs.hiltAndroidGradle.get().group!!,
                    libs.hiltAndroidGradle.get().name
                )
            }
        }
    }

    dependencies {
        classpath(libs.androidToolsBuildGradle)
        classpath(libs.kotlinGradle)
        classpath(libs.hiltAndroidGradle)
        classpath(libs.benManes)
    }
}

apply(plugin = libs.plugins.benManes.get().pluginId)


tasks {
    withType<DependencyUpdatesTask> {
        rejectVersionIf {
            candidate.version.isNonStable()
        }
    }
}

allprojects {
    tasks {
        withType<KotlinCompile>().configureEach {
            kotlinOptions{
                jvmTarget = SdkVersionProperties.Companion.DEFAULT.jvmTarget
                // Treat all Kotlin warnings as errors
                allWarningsAsErrors = false
                freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlinx.coroutines.FlowPreview"
                freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.Experimental"
                freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
                freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
                freeCompilerArgs = freeCompilerArgs + "-Xjvm-default=all"
            }
        }
    }
}

fun String.isNonStable(): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(this)
    return isStable.not()
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}
