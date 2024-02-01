package net.nomia.gradle

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware

class Version : Plugin<Project> {
    override fun apply(project: Project) {
        project.task("Version") {
            var versionName: String? = null
            project.gradle.projectsEvaluated {
                versionName = project.android.defaultConfig.versionName
            }
            doLast { println(versionName) }
        }
    }
}

@get:SuppressWarnings("TopLevelPropertyNaming")
val org.gradle.api.Project.`android`: BaseAppModuleExtension
    get() = (this as ExtensionAware).extensions.getByName("android") as BaseAppModuleExtension
