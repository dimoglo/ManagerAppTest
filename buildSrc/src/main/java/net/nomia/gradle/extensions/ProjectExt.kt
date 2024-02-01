package net.nomia.gradle

import org.gradle.api.Project
import java.util.Properties

fun Project.getVersionCodeFromProperties(): Int {
    val versionProperties = loadVersionProperties()
    val major = (versionProperties.getProperty("major").toIntOrNull() ?: 0) * 100000
    val minor = (versionProperties.getProperty("minor").toIntOrNull() ?: 0) * 1000
    val path = versionProperties.getProperty("patch").toIntOrNull() ?: 0
    return major + minor + path
}

@SuppressWarnings("MaxLineLength")
fun Project.getVersionNameFromProperties(): String {
    val versionProperties = loadVersionProperties()
    return "${versionProperties.getProperty("major")}.${versionProperties.getProperty("minor")}.${versionProperties.getProperty("patch")}"
}

fun Project.loadVersionProperties(): Properties {
    val versionProperties = Properties()
    versionProperties.load(project.file("version.properties").inputStream())
    return versionProperties
}
