val koinVersion: String by project
val ktorVersion: String by project

plugins {
    id("org.jetbrains.kotlin.plugin.serialization") version "1.4.32"
}

group = "com.shouman.reseller"
version = "0.0.1"

dependencies {
    // Koin for Kotlin
    api("org.koin:koin-ktor:$koinVersion")
    api("io.ktor:ktor-serialization:$ktorVersion")
}

