import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.shadowjar)
}

val executableName = "service"
val mainPackageName = "ru.punchman.crossconf24.example"

project.tasks.register<ShadowJar>("jvmBinary") {
    group = "build"
    manifest {
        attributes("Main-Class" to "$mainPackageName.MainKt")
    }
    archiveBaseName.set(executableName)
    archiveVersion.set("")
    archiveClassifier.set("")

    project.configure<KotlinMultiplatformExtension> {
        targets
            .filter { it.platformType == KotlinPlatformType.jvm && it.targetName == "jvm" }
            .flatMap { it.compilations }
            .filter { it.compilationName == "main" }
            .filterIsInstance<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJvmCompilation>()
            .forEach { main ->
                from(main.output)
                configurations = configurations + listOf(
                    main.compileDependencyFiles as Configuration,
                    main.runtimeDependencyFiles as Configuration,
                )
            }
    }
}

kotlin {
    jvm()
    macosArm64 { configureBinaries() }
    macosX64 { configureBinaries() }
    linuxX64 { configureBinaries() }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlin.serialization)
                implementation(libs.kotlin.serialization.json)
                implementation(libs.ktor.serialization.json)
                implementation(libs.ktor.server.core)
                implementation(libs.ktor.server.cio)
                implementation(libs.arrow)
            }
        }
    }
}

fun KotlinNativeTarget.configureBinaries() {
    binaries {
        executable(listOf(DEBUG, RELEASE)) {
            entryPoint = "$mainPackageName.main"
            this.baseName = executableName
        }
    }
}
