import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.kotlinx(module: String, version: String): Any =
    "org.jetbrains.kotlinx:kotlinx-$module:$version"