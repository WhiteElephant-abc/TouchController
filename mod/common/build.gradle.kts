import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
}

version = "0.0.13"
group = "top.fifthlight.touchcontroller"

sourceSets.main {
    kotlin.srcDir(project(":mod:resources").layout.buildDirectory.dir("generated/kotlin"))
}

tasks.compileKotlin {
    dependsOn(":mod:resources:generate")
}

dependencies {
    implementation(project(":common-data"))
    implementation(project(":proxy-client"))
    implementation(project(":proxy-server"))

    implementation(libs.compose.runtime)
    implementation(project(":combine"))

    implementation(libs.koin.core)
    implementation(libs.koin.compose)
    implementation(libs.koin.logger.slf4j)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)
}

kotlin {
    jvmToolchain(8)

    compilerOptions {
        apiVersion.set(KotlinVersion.KOTLIN_1_8)
        freeCompilerArgs.add("-Xconsistent-data-class-copy-visibility")
    }
}
