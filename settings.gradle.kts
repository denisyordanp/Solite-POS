pluginManagement {
    repositories {
        google()
        mavenCentral()

        maven {url = uri("https://plugins.gradle.org/m2/")}
    }
}

rootProject.name = "Solite-POS"
include("app")
include(":data")
