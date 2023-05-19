import org.jetbrains.compose.desktop.application.dsl.TargetFormat
plugins {
    kotlin("jvm") version "1.8.20"
    id("org.jetbrains.compose") version "1.4.0"
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
    implementation("origami:origami:4.7.0-10")
    implementation("origami:filters:1.39")
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