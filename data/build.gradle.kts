val exposedVersion: String by project
val h2Version: String by project
val ktorVersion: String by project

plugins {
}

group = "com.shouman.reseller"
version = "0.0.1"

dependencies {

    implementation(project(":domain"))

    // Database
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("com.h2database:h2:$h2Version")
    implementation("org.apache.commons:commons-lang3:3.0")

    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-json:$ktorVersion")
    implementation("io.ktor:ktor-client-jackson:$ktorVersion")

    api ("com.google.firebase:firebase-admin:7.2.0")
}



