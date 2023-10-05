pluginManagement {
    repositories {
        google()
        mavenCentral()

        maven {url = uri("https://plugins.gradle.org/m2/")}
    }
}

rootProject.name = "Solite-POS"
include("app")
include("data")
include("domain")

// Feature
include(":feature:customerorder")

// Core
include(":core:ui")
include(":core:network")

// Common
include(":common:ui")
include(":common:utility")

// Schema
include(":schema:menu")
include(":core:extension")
