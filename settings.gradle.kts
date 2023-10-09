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
include(":domain:customerorder")

// Feature
include(":feature:customerorder")

// Core
include(":core:ui")
include(":core:network")
include(":core:database")
include(":core:extension")

// Common
include(":common:ui")
include(":common:utility")

// Schema
include(":schema:menu")
include(":schema:database")
include(":schema:ui")
