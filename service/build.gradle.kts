import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
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
            }
        }
    }
}

fun KotlinNativeTarget.configureBinaries() {
    binaries {
        executable(listOf(DEBUG, RELEASE)) {
            entryPoint = "ru.punchman.crossconf24.example.main"
            this.baseName = "service"
        }
    }
}
