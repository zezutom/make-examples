plugins {
    kotlin("jvm") version "1.7.0" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.0" apply false
    id("com.github.johnrengelman.shadow") version "7.1.2" apply false
}

group = "com.tomaszezula.make"
version = "0.0.1"

allprojects {
    repositories {
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
        }
    }

}