import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.psi.addRemoveModifier.addModifier

plugins {
    kotlin("jvm") version "1.8.20"
    id("org.jetbrains.compose") version "1.5.12"
}


group = "origami"
version = "1.0"

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    maven {
        url = uri("https://repository.hellonico.info/repository/hellonico/")
    }
    maven {
        url = uri("https://clojars.org/repo/")
    }
}

//kotlin {
//    jvm {
//        jvmToolKit(19)
//        withJava()
//    }
//}
//tasks.withType<KotlinCompile>() {
//    kotlinOptions {
//      jvmTarget = "19"
//    }
//}


dependencies {
    implementation(compose.desktop.currentOs)
    implementation("origami:origami:4.9.0-0")
    implementation("origami:filters:1.43")
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            // Clojure needs this
            modules("java.sql")
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "origami-webcam-compose"
            packageVersion = "1.0.0"

macOS {
    bundleID = "hellonico.info.webcam"
}   
        }
        buildTypes {
            release {
                proguard.isEnabled.value(false)
            }
        }
    }
}
