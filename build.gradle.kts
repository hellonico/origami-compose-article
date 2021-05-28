import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.30"
    id("org.jetbrains.compose") version "0.3.1"
}

group = "me.niko"
version = "1.0"

repositories {
    jcenter()
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    maven {
        url = uri("https://repository.hellonico.info/repository/hellonico/")
    }
    maven {
        url = uri("https://clojars.org/repo/")
    }
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("origami:origami:4.5.1-3")
    implementation("origami:filters:1.20")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "articlecompose"
            packageVersion = "1.0.0"
        }
    }
}