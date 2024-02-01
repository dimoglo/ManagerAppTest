rootProject.name = "Manager App"

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenLocal()
        mavenCentral()
        google()
        maven("https://androidx.dev/snapshots/builds/7633184/artifacts/repository")
        maven("https://jitpack.io")
    }
}

include(":pos:app")
include(":core:core")
include(":common-ui")
include(":pos:common-data")
include(":pos:erp-schema")
include(":pos:erp-api")
include(":pos:main")
include(":settings")
include(":core:mvi-coroutines")
include(":core:mvi-android")

include(":core:common:converter")
include(":core:common:state-handler")
include(":core:common:utils")
include(":core:ui:compose")
include(":core:ui:splash_screen")
include(":core:ui:preview")
include(":core:ui:status-bar-controller")
include(":core:common:config-provider")
include(":core:common:serializer")
include(":core:common:network")
